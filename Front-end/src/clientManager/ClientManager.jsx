import React from "react";
import { Space, Table, Tag } from "antd";
import { IoIosArrowDown } from "react-icons/io";
import { FiPlus } from "react-icons/fi";
export default function ClientManager() {
  const columns = [
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Id</span>,
      dataIndex: "id",
      key: "id",

      render: (text) => <span>{text}</span>,
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Name</span>,
      dataIndex: "name",
      key: "name",
      style: { fontSize: "18px" },
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Gmail</span>,
      dataIndex: "gmail",
      key: "gmail",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Phone</span>,
      key: "phone",
      dataIndex: "phone",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Stauts</span>,
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
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Action</span>,
      key: "action",
      render: (_, record) => (
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center",
          }}
        >
          <p style={{ margin: 0 }}>Delete</p>
          <span style={{ margin: "0 8px" }}>|</span>
          <p style={{ margin: 0 }}>Delete</p>
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

  return (
    <div style={{ padding: "3%" }} className="">
      <p style={{ margin: 0, fontSize: 24 }} className="fw-bolder">
        Welcome, K!
      </p>
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
          <div
            style={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
              padding: "5px 10px",
              backgroundColor: "rgba(101, 101, 101, 1)",
              gap: 7,
              borderRadius: 5,
            }}
          >
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>
              Permissions
            </p>
            <p
              style={{
                margin: 0,
                fontSize: 20,
                color: "rgba(255, 139, 55, 1)",
              }}
            >
              All
            </p>
            <IoIosArrowDown color="rgba(224, 215, 234, 1)" />
          </div>
          <div
            style={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "space-between",
              padding: "5px 10px",
              backgroundColor: "rgba(101, 101, 101, 1)",
              gap: 80,
              borderRadius: 5,
            }}
          >
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>Joined</p>

            <IoIosArrowDown color="rgba(224, 215, 234, 1)" />
          </div>
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
              padding: "5px 10px",
              backgroundColor: "rgba(101, 101, 101, 1)",
              gap: 7,
              borderRadius: 5,
            }}
          >
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>Export</p>
          </div>
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
            {" "}
            <FiPlus color="rgba(224, 215, 234, 1)" />
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>New User</p>
          </div>
        </div>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        pagination={{ pageSize: 10 }}
      />
    </div>
  );
}
