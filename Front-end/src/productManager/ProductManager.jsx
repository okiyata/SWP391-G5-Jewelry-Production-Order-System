import React, { useState } from "react";
import { Table, Button, Modal, Form, Input } from "antd";
import { FiPlus } from "react-icons/fi";
import { CodeSandboxOutlined } from "@ant-design/icons";
import { useAuth } from "../provider/AuthProvider";

export default function ProductManager() {
  const { account } = useAuth();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isCreateModalVisible, setIsCreateModalVisible] = useState(false);
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [deleteProduct, setDeleteProduct] = useState(null);
  const [data, setData] = useState([
    {
      id: "ID0001",
      name: "Platinum emerald cut sapphire and round brilliant diamond earrings",
      description: "A high-end jewelry piece.",
    },
    {
      id: "ID0002",
      name: "Platinum round Ceylon sapphire and diamond cufflinks and tuxedo stud set",
      description: "A high-end jewelry piece.",
    },
    {
      id: "0.0003",
      name: "Tri-color gold free form band",
      description: "A high-end jewelry piece.",
    },
    {
      id: "ID0004",
      name: "Platinum wedding set filigree hand engraved",
      description: "A high-end jewelry piece.",
    },
    {
      id: "ID_0005",
      name: "Gold tahitian pearl pendant",
      description: "A high-end jewelry piece.",
    },
    {
      id: "ID_0006",
      name: "Platinum blue zircon ring accented with sapphires and diamonds",
      description: "A high-end jewelry piece.",
    },
  ]);

  const handleEdit = (record) => {
    setSelectedProduct(record);
    setIsModalVisible(true);
  };

  const handleDeleteClick = (record) => {
    setDeleteProduct(record);
    setIsDeleteModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    setSelectedProduct(null);
  };

  const handleAddCancel = () => {
    setIsCreateModalVisible(false);
  };

  const handleSave = (values) => {
    const newData = data.map((item) => (item.id === values.id ? values : item));
    setData(newData);
    setIsModalVisible(false);
    setSelectedProduct(null);
  };

  const handleAdd = (values) => {
    const newData = [...data, values];
    setData(newData);
    setIsCreateModalVisible(false);
  };

  const handleConfirmDelete = () => {
    const newData = data.filter((item) => item.id !== deleteProduct.id);
    setData(newData);
    setIsDeleteModalVisible(false);
    setDeleteProduct(null);
  };

  const handleCancelDelete = () => {
    setIsDeleteModalVisible(false);
    setDeleteProduct(null);
  };

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
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Description</span>,
      dataIndex: "description",
      key: "description",
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
            onClick={() => handleEdit(record)}
          >
            Edit
          </span>
        </div>
      ),
    },
  ];

  return (
    <div style={{ padding: "3%" }}>
      <p style={{ margin: 0, fontSize: 24, fontWeight: "bold" }}>Welcome, K!</p>
      <p style={{ fontSize: 16 }}>Products Manager</p>
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
        ></div>
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center",
            gap: 20,
          }}
        >
          <div
            onClick={() => setIsCreateModalVisible(true)}
            style={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "space-between",
              padding: "5px 20px",
              backgroundColor: "rgba(101, 101, 101, 1)",
              gap: 10,
              borderRadius: 5,
              cursor: "pointer",
            }}
          >
            <FiPlus color="rgba(224, 215, 234, 1)" />
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>
              New Product
            </p>
          </div>
        </div>
      </div>
      <Table columns={columns} dataSource={data} pagination={{ pageSize: 10 }} />
      <Modal
        title="Edit Product"
        visible={isModalVisible}
        onCancel={handleCancel}
        footer={null}
      >
        {selectedProduct && (
          <Form layout="vertical" initialValues={selectedProduct} onFinish={handleSave}>
            <Form.Item label="ID" name="id">
              <Input disabled />
            </Form.Item>
            <Form.Item label="Name" name="name">
              <Input />
            </Form.Item>
            <Form.Item label="Description" name="description">
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
      <Modal
        title="Confirm Delete"
        visible={isDeleteModalVisible}
        onOk={handleConfirmDelete}
        onCancel={handleCancelDelete}
      >
        <p>Are you sure you want to delete this product?</p>
      </Modal>
      <Modal
        title="Create Product"
        visible={isCreateModalVisible}
        onCancel={handleAddCancel}
        footer={null}
      >
        <div className="text-5xl mb-5">
          <CodeSandboxOutlined />{" "}
          <span className="text-4xl ml-3 font-medium">Product</span>
        </div>
        <Form layout="vertical" onFinish={handleAdd}>
          <Form.Item label="ID" name="id">
            <Input />
          </Form.Item>
          <Form.Item label="Name" name="name">
            <Input />
          </Form.Item>
          <Form.Item label="Description" name="description">
            <Input />
          </Form.Item>
          <Form.Item>
            <Button
              style={{ backgroundColor: "#000000", color: "white" }}
              htmlType="submit"
            >
              Save changes
            </Button>
            <Button
              onClick={handleAddCancel}
              style={{
                marginLeft: 8,
                backgroundColor: "#000000",
                color: "white",
              }}
            >
              Back
            </Button>
            <Button
              onClick={handleAddCancel}
              style={{
                marginLeft: 8,
                backgroundColor: "#000000",
                color: "white",
              }}
            >
              Create Your Dream Jewelry.
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
