'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const imageInput = document.querySelector('#imageInput');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');
const roleSelectList = document.querySelector('#role-select');
const roleSelect = document.getElementById('role-select');


let stompClient = null;
let userId = null;
let userName = null;
let selectedUserId = null;
let currentUserRole = null;

async function connect(event) {
    event.preventDefault();

    userId = document.querySelector('#id').value.trim();
    if (userId) {
        try {
            const response = await fetch(`/user/check/${userId}`);
            if (response.ok) {
                const user = await response.json();
                onUserFound(user);
            } else if (response.status === 404) {
                alert('User not found. Please enter a valid ID.');
            } else {
                console.error('Error checking user:', response.statusText);
            }
        } catch (error) {
            console.error('Error checking user:', error);
        }
    } else {
        alert('Please enter a user ID.');
    }
}

function onUserFound(user) {
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    userName = user.name;

    stompClient.connect({}, onConnected, onError);
}

async function onConnected() {
    console.log('Connected to WebSocket');
    stompClient.subscribe(`/user/${userId}/queue/messages`, onMessageReceived);

    stompClient.subscribe(`/topic/public`, onMessageReceived);

    document.querySelector('#connected-user-fullname').textContent = userName;

    await fetchUserRole();
    await findAndDisplayConnectedUsers();
}

async function fetchUserRole() {
    const currentUserRoleResponse = await fetch(`/${userId}/role`);
    currentUserRole = await currentUserRoleResponse.text();
    return currentUserRole;
}

async function fetchUnreadMessages() {
    try {
        const unreadMessagesResponse = await fetch(`/unread-messages/${userId}`);
        if (unreadMessagesResponse.ok) {
            const unreadMessagesText = await unreadMessagesResponse.text();
            if (unreadMessagesText.trim().length > 0) { // Kiểm tra xem phản hồi có nội dung không
                const unreadMessages = JSON.parse(unreadMessagesText);
                unreadMessages.forEach(message => {
                    const notifiedUser = document.querySelector(`#${message.senderId}`);
                    if (notifiedUser) {
                        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
                        if (nbrMsg) {
                            nbrMsg.classList.remove('hidden');
                            nbrMsg.textContent = parseInt(nbrMsg.textContent) + 1;
                        }
                    }
                });
            } else {
                console.log('No unread messages found.');
            }
        } else if (unreadMessagesResponse.status === 204) {
            console.log('No content found for unread messages.');
        } else {
            console.error('Failed to fetch unread messages:', unreadMessagesResponse.statusText);
        }
    } catch (error) {
        console.error('Error fetching unread messages:', error);
    }
}


async function findAndDisplayConnectedUsers() {
    try {
        if (currentUserRole === "CUSTOMER") {
            roleSelectList.classList.add('hidden');
            const allUsersResponse = await fetch(`/users/Customer`);
            const users = await allUsersResponse.json();
            await renderConnectedUsers(users.filter(user => user.id !== userId));
        } else {
            roleSelectList.classList.remove('hidden');
            const allUsersResponse = await fetch(`/users/${roleSelect.value}`);
            const users = await allUsersResponse.json();
            await renderConnectedUsers(users.filter(user => user.id !== userId));
        }

        await fetchUnreadMessages(); // Kiểm tra và hiển thị tin nhắn chưa đọc
    } catch (error) {
        console.error('Error fetching and displaying connected users:', error);
    }
}


function renderConnectedUsers(users) {
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = ''; // Clear the list

    users.forEach(user => {
        const listItem = createUserElement(user);
        connectedUsersList.appendChild(listItem);
    });
}

function createUserElement(user) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.id;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.id;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.name;

    const statusContainer = document.createElement('div');
    statusContainer.classList.add('status-container');

    const statusSpan = document.createElement('span');
    statusSpan.textContent = '●';
    statusSpan.classList.add(user.status === 'ONLINE' ? 'online' : 'offline');

    const statusTextSpan = document.createElement('span');
    statusTextSpan.textContent = user.status === 'ONLINE' ? 'Online' : 'Offline';
    statusTextSpan.classList.add(user.status === 'ONLINE' ? 'online' : 'offline');

    statusContainer.appendChild(statusSpan);
    statusContainer.appendChild(statusTextSpan);

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(statusContainer);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    return listItem;
}

function onRoleChange() {
    const selectedRole = roleSelect.value;
    findAndDisplayConnectedUsers(selectedRole).then();
}

async function markMessagesAsRead(recipientId) {
    try {
        const response = await fetch(`/mark-messages-as-read/${recipientId}`, {
            method: 'POST'
        });
        if (response.ok) {
            console.log(`Messages for ${recipientId} marked as read.`);
        } else {
            console.error(`Failed to mark messages as read for ${recipientId}: ${response.statusText}`);
        }
    } catch (error) {
        console.error(`Error marking messages as read for ${recipientId}:`, error);
    }
}

async function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    await fetchAndDisplayUserChat();

    await markMessagesAsRead(userId);

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === userId) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }

    let messageElement;
    if (content.startsWith('https://')) {
        messageElement = document.createElement('img');
        messageElement.src = content;
        messageElement.alt = 'Uploaded image';
        messageElement.classList.add('uploaded-image'); // Thêm lớp CSS để định dạng ảnh
    } else {
        messageElement = document.createElement('p');
        messageElement.textContent = content;
    }

    messageContainer.appendChild(messageElement);
    chatArea.appendChild(messageContainer);

    // Cuộn xuống cuối khi có tin nhắn mới
    chatArea.scrollTop = chatArea.scrollHeight;
}


async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${userId}/${selectedUserId}`);
    console.log('User chat response', userChatResponse);
    if (userChatResponse.status === 200) {
        const userChat = await userChatResponse.json();
        chatArea.innerHTML = '';
        userChat.forEach(chat => {
            displayMessage(chat.senderId, chat.content);
        });
        chatArea.scrollTop = chatArea.scrollHeight;
    } else {
        chatArea.innerHTML = '';
        console.log('No chat messages found.');
    }
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
    console.error('WebSocket connection error:', error);
}

function sendMessage(event) {
    event.preventDefault();

    const messageContent = messageInput.value.trim();
    if ((messageContent) && stompClient) {
        const chatMessage = {
            senderId: userId,
            recipientId: selectedUserId,
            content: messageContent, // imageUrl là URL của hình ảnh đã tải lên Firebase Storage
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        console.log('Message sent:', chatMessage);
        displayMessage(userId, messageContent); // Hiển thị hình ảnh hoặc nội dung tin nhắn
        messageInput.value = '';
    }

    chatArea.scrollTop = chatArea.scrollHeight;
}

async function onMessageReceived(payload) {
    console.log('PAYLOAD', payload);
    const message = JSON.parse(payload.body);

    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        console.log('Gui duoc roi neeee');
        chatArea.scrollTop = chatArea.scrollHeight;
    } else {
        const notifiedUser = document.querySelector(`#${message.senderId}`);
        if (notifiedUser && !notifiedUser.classList.contains('active')) {
            const nbrMsg = notifiedUser.querySelector('.nbr-msg');
            if (nbrMsg) {
                nbrMsg.classList.remove('hidden');
                nbrMsg.textContent = parseInt(nbrMsg.textContent) + 1;
            }
            console.log('Message notification sent:', nbrMsg);
        }
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
        console.log('Active user item:', selectedUserId);
    } else {
        messageForm.classList.add('hidden');
        console.log('Hidden message form');
    }
}


function onLogout() {
    if (stompClient) {
        stompClient.send("/app/user.disconnectUser", {},
            JSON.stringify({id: userId, status: 'OFFLINE'})
        );
        stompClient.disconnect();
    }
    window.location.reload();
}

async function handleImageUpload(event) {
    const imageFile = event.target.files[0];
    if (imageFile) {
        try {
            const formData = new FormData();
            formData.append('senderId', userId);
            formData.append('recipientId', selectedUserId);
            formData.append('message', messageInput.value);

            // Check image size and resize if necessary
            const resizedImageFile = await resizeImage(imageFile);
            formData.append('file', resizedImageFile);

            const response = await fetch('/chat/upload', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                throw new Error('Failed to upload image');
            }

            const imageURL = await response.text();
            displayMessage(userId, imageURL);

            messageInput.value = '';
            imageInput.value = '';
        } catch (error) {
            console.error('Error uploading image:', error);
            alert('Failed to upload image. Please try again later.');
        }
    }
}

async function resizeImage(imageFile) {
    const maxSize = 1024; // max width or height
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(imageFile);
        reader.onload = function(event) {
            const img = new Image();
            img.src = event.target.result;
            img.onload = function() {
                let width = img.width;
                let height = img.height;

                if (width > height) {
                    if (width > maxSize) {
                        height *= maxSize / width;
                        width = maxSize;
                    }
                } else {
                    if (height > maxSize) {
                        width *= maxSize / height;
                        height = maxSize;
                    }
                }

                const canvas = document.createElement('canvas');
                canvas.width = width;
                canvas.height = height;
                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0, width, height);

                canvas.toBlob((blob) => {
                    resolve(new File([blob], imageFile.name, { type: imageFile.type }));
                }, imageFile.type);
            };
        };
        reader.onerror = reject;
    });
}


usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
document.getElementById('imageInput').addEventListener('change', handleImageUpload);

document.addEventListener('DOMContentLoaded', (event) => {
    roleSelect.addEventListener('change', onRoleChange);
});