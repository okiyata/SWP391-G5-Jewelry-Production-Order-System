import React from "react";
import { Space, Table, Tag } from "antd";
import { HiOutlineUserGroup } from "react-icons/hi";
import { IoMdCart } from "react-icons/io";
import { LiaUser } from "react-icons/lia";
import LineChartComponent from "../chart/LineChart";

export default function DashboardManager() {
  const columns = [
    {
      title: <span style={{ fontSize: 18, fontWeight: 400 }}>Id</span>,
      dataIndex: "id",
      key: "id",
      render: (text) => <span>{text}</span>,
    },
    {
      title: <span style={{ fontSize: 18, fontWeight: 400 }}>Name</span>,
      dataIndex: "name",
      key: "name",
      style: { fontSize: "18px" },
    },
    {
      title: <span style={{ fontSize: 18, fontWeight: 400 }}>Gmail</span>,
      dataIndex: "gmail",
      key: "gmail",
    },
    {
      title: <span style={{ fontSize: 18, fontWeight: 400 }}>Phone</span>,
      key: "phone",
      dataIndex: "phone",
    },
    {
      title: <span style={{ fontSize: 18, fontWeight: 400 }}>Status</span>,
      key: "status",
      dataIndex: "status",
      render: (_, record) => (
        <Space size="middle">
          {record.status === "active" ? (
            <Tag color="green">Active</Tag>
          ) : (
            <Tag color="red">Inactive</Tag>
          )}
        </Space>
      ),
    },
    {
      title: <span style={{ fontSize: 18, fontWeight: 400 }}>Action</span>,
      key: "action",
      render: (_, record) => (
        <div style={{ display: "flex", flexDirection: "row", alignItems: "center" }}>
          <p style={{ margin: 0, cursor: "pointer", color: "blue" }}>Edit</p>
          <span style={{ margin: "0 8px" }}>|</span>
          <p style={{ margin: 0, cursor: "pointer", color: "blue" }}>Delete</p>
        </div>
      ),
    },
  ];

  const data = [
    {
      id: "CL_0001",
      name: "John Brown",
      phone: "01234556",
      gmail: "New York No. 1 Lake Park",
      status: "active",
    },
    {
      id: "CL_0002",
      name: "Jim Green",
      phone: "01234556",
      gmail: "London No. 1 Lake Park",
      status: "inactive",
    },
    {
      id: "CL_0003",
      name: "Joe Black",
      phone: "01234556",
      gmail: "Sydney No. 1 Lake Park",
      status: "active",
    },
    {
      id: "CL_0004",
      name: "John Brown",
      phone: "01234556",
      gmail: "New York No. 1 Lake Park",
      status: "active",
    },
    {
      id: "CL_0005",
      name: "Jim Green",
      phone: "01234556",
      gmail: "London No. 1 Lake Park",
      status: "inactive",
    },
    {
      id: "CL_0006",
      name: "Joe Black",
      phone: "01234556",
      gmail: "Sydney No. 1 Lake Park",
      status: "active",
    },
    {
      id: "CL_0007",
      name: "John Brown",
      phone: "01234556",
      gmail: "New York No. 1 Lake Park",
      status: "active",
    },
    {
      id: "CL_0008",
      name: "Jim Green",
      phone: "01234556",
      gmail: "London No. 1 Lake Park",
      status: "inactive",
    },
    {
      id: "CL_0009",
      name: "Joe Black",
      phone: "01234556",
      gmail: "Sydney No. 1 Lake Park",
      status: "active",
    },
  ];

  const columnsOrder = [
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
  ];

  const dataOrder = [
    {
      orderId: "ID_0001",
      customerID: "CL_0009",
      date: "11/6/1994",
      total: "$1200",
      status: "unpaid",
    },
    {
      orderId: "ID_0001",
      customerID: "CL_0009",
      date: "11/6/1994",
      total: "$1200",
      status: "paid",
    },
    {
      orderId: "ID_0001",
      customerID: "CL_0009",
      date: "11/6/1994",
      total: "$1200",
      status: "unpaid",
    },
    {
      orderId: "ID_0001",
      customerID: "CL_0009",
      date: "11/6/1994",
      total: "$1200",
      status: "paid",
    },
    {
      orderId: "ID_0001",
      customerID: "CL_0009",
      date: "11/6/1994",
      total: "$1200",
      status: "unpaid",
    },
  ];

  return (
    <div style={{ padding: "3%" }} className="">
      <p style={{ margin: 0, fontSize: 24 }} className="fw-bolder">
        Welcome, K!
      </p>
      <p style={{ fontSize: 16 }}>Dashboard</p>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr 1fr 1fr",
          gap: 20,
          marginBottom: "1%",
        }}
      >
        <div
          style={{
            borderWidth: 1,
            borderColor: "rgba(0, 0, 0, 0.15)   ",
            borderStyle: "solid",
            borderRadius: 5,
            padding: "12px 12px",
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <div
            style={{
              borderRadius: 5,
              padding: "0 16px",
              display: "flex",
              alignItems: "center",
              backgroundColor: "rgba(229, 229, 229, 1)",
            }}
          >
            <HiOutlineUserGroup size={24} color="rgba(163, 163, 163, 1)" />
          </div>

          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "flex-end",
            }}
          >
            <p style={{ margin: 0, fontSize: 17, fontWeight: 400 }}>Client</p>
            <p style={{ margin: 0, fontSize: 20, fontWeight: 500 }}>126</p>
          </div>
        </div>
        <div
          style={{
            borderWidth: 1,
            borderColor: "rgba(0, 0, 0, 0.15)   ",
            borderStyle: "solid",
            borderRadius: 5,
            padding: "12px 12px",
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <div
            style={{
              borderRadius: 5,
              padding: "0 16px",
              display: "flex",
              alignItems: "center",
              backgroundColor: "rgba(229, 229, 229, 1)",
            }}
          >
            <LiaUser size={24} color="rgba(163, 163, 163, 1)" />
          </div>

          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "flex-end",
            }}
          >
            <p style={{ margin: 0, fontSize: 17, fontWeight: 400 }}>
              Employees
            </p>
            <p style={{ margin: 0, fontSize: 20, fontWeight: 500 }}>18</p>
          </div>
        </div>{" "}
        <div
          style={{
            borderWidth: 1,
            borderColor: "rgba(0, 0, 0, 0.15)   ",
            borderStyle: "solid",
            borderRadius: 5,
            padding: "12px 12px",
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <div
            style={{
              borderRadius: 5,
              padding: "0 16px",
              display: "flex",
              alignItems: "center",
              backgroundColor: "rgba(229, 229, 229, 1)",
            }}
          >
            <IoMdCart size={24} color="rgba(163, 163, 163, 1)" />
          </div>

          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "flex-end",
            }}
          >
            <p style={{ margin: 0, fontSize: 17, fontWeight: 400 }}>Orders</p>
            <p style={{ margin: 0, fontSize: 20, fontWeight: 500 }}>56</p>
          </div>
        </div>{" "}
        <div
          style={{
            borderWidth: 1,
            borderColor: "rgba(0, 0, 0, 0.15)   ",
            borderStyle: "solid",
            borderRadius: 5,
            padding: "12px 12px",
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <div
            style={{
              borderRadius: 5,
              padding: "0 16px",
              display: "flex",
              alignItems: "center",
              backgroundColor: "rgba(229, 229, 229, 1)",
            }}
          >
            <p
              style={{
                margin: 0,
                fontSize: 24,
                color: "rgba(163, 163, 163, 1)",
              }}
            >
              $
            </p>
          </div>

          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "flex-end",
            }}
          >
            <p style={{ margin: 0, fontSize: 17, fontWeight: 400 }}>Revenue</p>
            <p style={{ margin: 0, fontSize: 20, fontWeight: 500 }}>125k</p>
          </div>
        </div>
      </div>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr",
          gap: 20,
          marginBottom: "1%",
        }}
      >
        <div
          style={{
            borderWidth: 1,
            borderColor: "rgba(0, 0, 0, 0.15)   ",
            borderStyle: "solid",
            borderRadius: 5,
            height: "100%",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            padding: "10px 10px",
          }}
        >
          <h1
            style={{
              textAlign: "center",
              margin: 0,
              fontSize: 24,
              fontWeight: 500,
            }}
          >
            Renvenue per month
          </h1>
          <div style={{ flex: 1 }}>
            <LineChartComponent />
          </div>
        </div>
        <div
          style={{
            borderWidth: 1,
            borderColor: "rgba(0, 0, 0, 0.15)   ",
            borderStyle: "solid",
            borderRadius: 5,
            display: "flex",
            flexDirection: "column",
            height: "100%",
            padding: "10px 10px",
          }}
        >
          <p style={{ margin: 0, fontSize: 20 }} className="fw-bolder">
            Orders
          </p>
          <Table
            columns={columnsOrder}
            dataSource={dataOrder}
            pagination={{ pageSize: 10 }}
            rowClassName={{
              padding: "5px",
            }}
            style={{ height: "100%" }}
          />
        </div>
      </div>
      <p style={{ margin: 0, fontSize: 20 }} className="fw-bolder">
        Client
      </p>
      <Table
        columns={columns}
        dataSource={data}
        pagination={{ pageSize: 10 }}
        rowClassName={{
          padding: "5px",
        }}
      />
    </div>
  );
}
