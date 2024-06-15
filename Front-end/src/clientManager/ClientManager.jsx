import React, { useState } from "react";
import { Space, Table, Tag, Button, Modal, Form, Input, Select } from "antd";
import { FiPlus } from "react-icons/fi";

export default function ClientManager() {
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  const columns = [
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>ID</span>,
      dataIndex: "id",
      key: "id",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Name</span>,
      dataIndex: "name",
      key: "name",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Gmail</span>,
      dataIndex: "gmail",
      key: "gmail",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Phone</span>,
      dataIndex: "phone",
      key: "phone",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Status</span>,
      key: "status",
      dataIndex: "status",
      render: (text) => (
        <Tag color={text === "active" ? "green" : "red"}>
          {text.charAt(0).toUpperCase() + text.slice(1)}
        </Tag>
      ),
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Action</span>,
      key: "action",
      render: (_, record) => (
        <div style={{ display: "flex", alignItems: "center" }}>
          <span style={{ cursor: "pointer", color: "blue" }}>Delete</span>
          <span style={{ margin: "0 8px" }}>|</span>
          <span
            style={{ cursor: "pointer", color: "blue" }}
            onClick={() => handleEdit(record)}
          >
            Edit
          </span>
        </div>
      ),
    },
  ];

  const data = [
    { id: "CL_0001", name: "Tran Mai Quang Khai", gmail: "khaitmq@gmail.com", phone: "0867406725", status: "active" },
    { id: "CL_0002", name: "Nguyen Hoang Dung", gmail: "dungnh@gmail.com", phone: "0574179547", status: "inactive" },
    { id: "CL_0003", name: "Vu Tien Dat", gmail: "datvt@gmail.com", phone: "0936127853", status: "active" },
    { id: "CL_0004", name: "Nguyen Viet Thai", gmail: "thainv@gmail.com", phone: "0826709871", status: "active" },
    { id: "CL_0005", name: "Bui Khanh Duy", gmail: "duybkse73484@gmail.com", phone: "0936137090", status: "active" },
    { id: "CL_0006", name: "Ly Hoang Khang", gmail: "khang@gmail.com", phone: "0845123898", status: "active" },
    { id: "CL_0007", name: "Ha Duy Tung", gmail: "tung@gmail.com", phone: "091834926", status: "inactive" },
    { id: "CL_0008", name: "Doan Dang Thien Bao", gmail: "bao@gmail.com", phone: "0938110083", status: "active" },
    { id: "CL_0009", name: "Nguyen Huu Quoc Hung", gmail: "hung@gmail.com", phone: "0965326132", status: "inactive" },
    { id: "CL_0010", name: "Duong Hong An", gmail: "An@gmail.com", phone: "0987665512", status: "active" },
  ];

  const handleEdit = (record) => {
    setSelectedUser(record);
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    setSelectedUser(null);
  };

  const handleSave = (values) => {
    console.log("Saved values:", values);
    setIsModalVisible(false);
    setSelectedUser(null);
  };

  return (
    <div style={{ padding: "3%" }}>
      <p style={{ margin: 0, fontSize: 24, fontWeight: 'bold' }}>Welcome, K!</p>
      <p style={{ fontSize: 16 }}>Clients Manager</p>
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
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
        </div>
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center",
            gap: 20,
          }}
        >
          <div
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
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>
              New User
            </p>
          </div>
        </div>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        pagination={{ pageSize: 10 }}
      />
      <Modal
        title="Edit User"
        visible={isModalVisible}
        onCancel={handleCancel}
        footer={null}
      >
        {selectedUser && (
          <Form
            layout="vertical"
            initialValues={selectedUser}
            onFinish={handleSave}
          >
            <Form.Item label="ID" name="id">
              <Input disabled />
            </Form.Item>
            <Form.Item label="Name" name="name">
              <Input />
            </Form.Item>
            <Form.Item label="Gmail" name="gmail">
              <Input />
            </Form.Item>
            <Form.Item label="Phone" name="phone">
              <Input />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit">
                Save changes
              </Button>
              <Button onClick={handleCancel} style={{ marginLeft: 8 }}>
                Back
              </Button>
            </Form.Item>
          </Form>
        )}
      </Modal>
    </div>
  );
}
