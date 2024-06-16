import React, { useState } from "react";
import { Table } from "antd";
import { FiPlus } from "react-icons/fi";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { Editor } from "@tinymce/tinymce-react";

export default function BlogManager() {
  const [open, setOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [currentBlog, setCurrentBlog] = useState(null);
  const [deleteBlog, setDeleteBlog] = useState(null);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const [blogs, setBlogs] = useState([
    {
      id: "BL_0001",
      title:
        "Customer engagement ring trends: elevating personal expressions of love",
      tag: "#engagement #rings #trends",
    },
    {
      id: "BL_0002",
      title: "How to clean your jewelry at home",
      tag: "#cleaning gemstones #cleaning gold jewelry #cleaning platinum jewelry #cleaning silver jewelry #custom jewelry",
    },
    {
      id: "BL_0003",
      title:
        "Customer engagement ring trends: elevating personal expressions of love",
      tag: "#engagement #rings #trends",
    },
    {
      id: "BL_0004",
      title:
        "Customer engagement ring trends: elevating personal expressions of love",
      tag: "#engagement #rings #trends",
    },
    {
      id: "BL_0005",
      title: "Jim Green",
      tag: "#cleaning gemstones #cleaning gold jewelry #cleaning platinum jewelry #cleaning silver jewelry #custom jewelry",
    },
    {
      id: "BL_0006",
      title: "Joe Black",
      tag: "#cleaning gemstones #cleaning gold jewelry #cleaning platinum jewelry #cleaning silver jewelry #custom jewelry",
    },
    {
      id: "BL_0007",
      title:
        "Customer engagement ring trends: elevating personal expressions of love",
      tag: "#engagement #rings #trends",
    },
    {
      id: "BL_0008",
      title: "Jim Green",
      tag: "#cleaning gemstones #cleaning gold jewelry #cleaning platinum jewelry #cleaning silver jewelry #custom jewelry",
    },
    {
      id: "BL_0009",
      title: "Joe Black",
      tag: "#cleaning gemstones #cleaning gold jewelry #cleaning platinum jewelry #cleaning silver jewelry #custom jewelry",
    },
  ]);

  const handleEditClick = (record) => {
    setCurrentBlog(record);
    setIsEditing(true);
    setOpen(true);
  };

  const handleDeleteClick = (record) => {
    setDeleteBlog(record);
    setDeleteModalVisible(true);
  };

  const handleConfirmDelete = () => {
    setBlogs(blogs.filter((blog) => blog.id !== deleteBlog.id));
    setDeleteModalVisible(false);
    setDeleteBlog(null);
  };

  const handleCancelDelete = () => {
    setDeleteModalVisible(false);
    setDeleteBlog(null);
  };

  const columns = [
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Id</span>,
      dataIndex: "id",
      key: "id",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Title</span>,
      dataIndex: "title",
      key: "title",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Tag</span>,
      dataIndex: "tag",
      key: "tag",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Action</span>,
      key: "action",
      render: (_, record) => (
        <div style={{ display: "flex", alignItems: "center" }}>
          <span
            style={{ cursor: "pointer", color: "blue" }}
            onClick={() => handleDeleteClick(record)}
          >
            Delete
          </span>
          <span style={{ margin: "0 8px" }}>|</span>
          <span
            style={{ cursor: "pointer", color: "blue" }}
            onClick={() => handleEditClick(record)}
          >
            Edit
          </span>
        </div>
      ),
    },
  ];

  const tags =
    "engagement rings trends cleaning gemstones cleaning gold jewelry cleaning platinum jewelry cleaning silver jewelry custom jewelry";

  return (
    <div style={{ padding: "3%" }}>
      <Modal size="lg" show={open} onHide={() => setOpen(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{isEditing ? "Edit Blog" : "New Blog"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="mb-3">
            <div className="text-sm mb-1 pl-1">Title</div>
            <textarea
              className="w-full rounded-lg px-2 py-2"
              defaultValue={isEditing ? currentBlog?.title : ""}
            ></textarea>
          </div>
          <div className="mb-3">
            <div className="text-sm mb-1 pl-1">Tags</div>
            <select className="rounded-lg pr-5 pl-2 py-2 w-1/3">
              {tags.split(" ").map((tag) => (
                <option
                  key={tag}
                  selected={isEditing && currentBlog?.tag.includes(tag)}
                >
                  {tag}
                </option>
              ))}
            </select>
          </div>
          <div className="mb-3">
            <div className="text-sm mb-1 pl-1">Content</div>
            <Editor
              className="w-full rounded-lg px-5 py-2"
              apiKey="rxzla8t3gi19lqs86mqzx01taekkxyk5yyaavvy8rwz0wi83"
              init={{
                plugins:
                  "anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount linkchecker",
                toolbar:
                  "undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table | align lineheight | numlist bullist indent outdent | emoticons charmap | removeformat",
              }}
              initialValue={isEditing ? currentBlog?.content : ""}
            />
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setOpen(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={() => setOpen(false)}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
      <Modal show={deleteModalVisible} onHide={handleCancelDelete}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Delete</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Are you sure you want to delete this blog?</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCancelDelete}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleConfirmDelete}>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>
      <p style={{ margin: 0, fontSize: 24, fontWeight: "bold" }}>Welcome, K!</p>
      <p style={{ fontSize: 16 }}>Blogs</p>
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "flex-end",
          marginBottom: "2%",
        }}
      >
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center",
            gap: 20,
          }}
        >
          <div
            onClick={() => {
              setIsEditing(false);
              setCurrentBlog(null);
              setOpen(true);
            }}
            style={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "space-between",
              padding: "5px 20px",
              backgroundColor: "rgba(101, 101, 101, 1)",
              gap: 10,
              borderRadius: 5,
            }}
          >
            <FiPlus color="rgba(224, 215, 234, 1)" />
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>New Blog</p>
          </div>
        </div>
      </div>
      <Table columns={columns} dataSource={blogs} pagination={{ pageSize: 10 }} />
    </div>
  );
}
