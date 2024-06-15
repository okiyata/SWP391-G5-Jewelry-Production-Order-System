import React, { useState } from "react";
import { Space, Table, Tag, Button } from "antd";
import { IoIosArrowDown } from "react-icons/io";
import { FiPlus } from "react-icons/fi";

export default function EmployeeManager() {
  const [filterStatus, setFilterStatus] = useState(null);

  const handleFilterChange = (event) => {
    const selectedValue = event.target.value;
    setFilterStatus(selectedValue);
  };

  const columns = [
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>ID</span>,
      dataIndex: "id",
      key: "id",
    },
    {
      title: <span style={{ fontSize: 20, fontWeight: 400 }}>Role</span>,
      dataIndex: "role",
      key: "role",
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
        <Space size="middle">
          <span style={{ cursor: "pointer", color: "blue" }}>Delete</span>
          <span style={{ margin: "0 5px" }}>|</span>
          <span style={{ cursor: "pointer", color: "blue" }}>Edit</span>
        </Space>
      ),
    },
  ];

  const data = [
    { id: "AD_0001",role: "Admin", name: "Tran Mai Quang Khai", gmail: "khaitmq@gmail.com", phone: "0867406725", status: "active" },
    { id: "MA_0002",role: "Manager", name: "Nguyen Hoang Dung", gmail: "dungnh@gmail.com", phone: "0574179547", status: "inactive" },
    { id: "SS_0003",role: "Sales Staff", name: "Vu Tien Dat", gmail: "datvt@gmail.com", phone: "0936127853", status: "active" },
    { id: "AD_0004",role: "Admin", name: "Nguyen Viet Thai", gmail: "thainv@gmail.com", phone: "0826709871", status: "active" },
    { id: "AD_0005",role: "Admin", name: "Bui Khanh Duy", gmail: "duybkse73484@gmail.com", phone: "0936137090", status: "active" },
    { id: "AD_0006",role: "Admin", name: "Ly Hoang Khang", gmail: "khang@gmail.com", phone: "0845123898", status: "active" },
    { id: "AD_0007",role: "Admin", name: "Ha Duy Tung", gmail: "tung@gmail.com", phone: "091834926", status: "inactive" },
    { id: "AD_0008",role: "Admin", name: "Doan Dang Thien Bao", gmail: "bao@gmail.com", phone: "0938110083", status: "active" },
    { id: "AD_0009",role: "Admin", name: "Nguyen Huu Quoc Hung", gmail: "hung@gmail.com", phone: "0965326132", status: "inactive" },
    { id: "CB_0010",role: "Contribution", name: "Duong Hong An", gmail: "An@gmail.com", phone: "0987665512", status: "active" },
  ];

  const filteredData = filterStatus
    ? data.filter((item) => item.status === filterStatus)
    : data;

  return (
    <div style={{ padding: "3%" }}>
      <p style={{ margin: 0, fontSize: 24, fontWeight: 'bold' }}>Welcome, K!</p>
      <p style={{ fontSize: 16 }}>Employee Manager</p>
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
            <select
              value={filterStatus}
              onChange={handleFilterChange}
              style={{
                margin: 0,
                fontSize: 20,
                color: "rgba(255, 139, 55, 1)",
                backgroundColor: "transparent",
                border: "none",
                outline: "none",
              }}
            >
              <option value="">All</option>
              <option value="admin">Admin</option>
              <option value="manager">Manager</option>
              <option value="sales_staff">Sales Staff</option>
              <option value="contribution">Contribution</option>
            </select>
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
            <FiPlus color="rgba(224, 215, 234, 1)" />
            <p style={{ margin: 0, fontSize: 20, color: "white" }}>
              Add Employee
            </p>
          </div>
        </div>
      </div>
      <Table
        columns={columns}
        dataSource={filteredData}
        pagination={{ pageSize: 10 }}
      />
    </div>
  );
}
