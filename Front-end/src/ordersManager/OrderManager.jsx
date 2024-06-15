import React, { useState } from "react";
import { Space, Table, Tag, Modal, Button } from "antd";
import { IoIosArrowDown } from "react-icons/io";
import { FiPlus } from "react-icons/fi";

export default function OrderManager() {
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);

  const columns = [
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>OrderID</span>,
      dataIndex: "orderId",
      key: "orderId",
      render: (text) => <span>{text}</span>,
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>CustomerID</span>,
      dataIndex: "customerID",
      key: "customerID",
      style: { fontSize: "18px" },
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Date</span>,
      dataIndex: "date",
      key: "date",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Total</span>,
      key: "total",
      dataIndex: "total",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Status</span>,
      key: "status",
      dataIndex: "status",
      render: (_, record) => (
        <Space size="middle">
          {record.status === "paid" ? (
            <Tag color="green">Paid</Tag>
          ) : (
            <Tag color="red">Unpaid</Tag>
          )}
        </Space>
      ),
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Detail</span>,
      key: "detail",
      render: (_, record) => (
        <Button onClick={() => showDetail(record)}>Show Detail</Button>
      ),
    },
  ];

  const data = [
    {
      orderId: "ID_0001",
      customerID: "CL_0009",
      date: "11/6/1994",
      total: "$1200",
      status: "paid",
      detail: "",
    },
    {
      orderId: "ID_0002",
      customerID: "CL_0001",
      date: "11/6/1994",
      total: "$11000",
      status: "unpaid",
      detail: "",
    },
    {
      orderId: "ID_0003",
      customerID: "CL_0006",
      date: "11/6/1994",
      total: "$1200",
      status: "paid",
      detail: "",
    },
    {
      orderId: "ID_0004",
      customerID: "CL_0001",
      date: "11/6/1994",
      total: "$11000",
      status: "unpaid",
      detail: "",
    },
    {
      orderId: "ID_0005",
      customerID: "CL_0002",
      date: "11/6/1994",
      total: "$11000",
      status: "unpaid",
      detail: "",
    },
    {
      orderId: "ID_0006",
      customerID: "CL_0007",
      date: "11/6/1994",
      total: "$11000",
      status: "paid",
      detail: "",
    },
  ];

  const showDetail = (order) => {
    setSelectedOrder(order);
    setIsModalVisible(true);
  };

  const handleOk = () => {
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  return (
    <div style={{ padding: "3%" }}>
      <p style={{ margin: 0, fontSize: 24 }} className="fw-bolder">
        Welcome, K!
      </p>
      <p style={{ fontSize: 16 }}>Order</p>

      <Table
        columns={columns}
        dataSource={data}
        pagination={{ pageSize: 10 }}
      />

      <Modal
        title="Order Details"
        visible={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
      >
        {selectedOrder && (
          <div>
            <p><strong>Order ID:</strong> {selectedOrder.orderId}</p>
            <p><strong>Customer ID:</strong> {selectedOrder.customerID}</p>
            <p><strong>Date:</strong> {selectedOrder.date}</p>
            <p><strong>Total:</strong> {selectedOrder.total}</p>
            <p><strong>Status:</strong> {selectedOrder.status}</p>
            <p><strong>Details:</strong> Details of the order go here...</p>
          </div>
        )}
      </Modal>
    </div>
  );
}
