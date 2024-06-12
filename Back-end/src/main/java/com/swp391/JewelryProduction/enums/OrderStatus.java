package com.swp391.JewelryProduction.enums;

public enum OrderStatus {
    REQUEST,
        REQUESTING,             //GO TO REQ_AWAIT_APPROVAL
        REQ_AWAIT_APPROVAL,     //
            REQ_APPROVAL_PROCESS,
            REQ_APPROVED,   //GO TO IN_EXCHANGING
            REQ_DECLINED,   //GO TO CANCEL
        IN_EXCHANGING,          //Guard from REQ_AWAIT_APPROVAL to IN_EXCHANGIN
    QUOTATION,
        AWAIT_QUO,
        QUO_AWAIT_MANA_APPROVAL,
            QUO_MANA_APPROVAL_PROCESS,
            QUO_MANA_APPROVED,
            QUO_MANA_DECLINED,
        QUO_AWAIT_CUST_APPROVAL,
            QUO_CUST_APPROVAL_PROCESS,
            QUO_CUST_APPROVED,
            QUO_CUST_DECLINED,
        AWAIT_TRANSACTION,
            TRANSACTION_PROCESS,
            TRANSACTION_APPROVED,
            TRANSACTION_DECLINED,
    DESIGN,
        IN_DESIGNING,
        DES_AWAIT_MANA_APPROVAL,
            DES_MANA_APPROVAL_PROCESS,
            DES_MANA_APPROVED,
            DES_MANA_DECLINED,
        DES_AWAIT_CUST_APPROVAL,
            DES_CUST_APPROVAL_PROCESS,
            DES_CUST_APPROVED,
            DES_CUST_DECLINED,
    PRODUCTION,
        IN_PRODUCTION,
        FINISHED,
        PRO_AWAIT_APPROVAL,
    TRANSPORT,
        SENT,
        DELIVERED,
    CANCEL,
    ORDER_RESTORED,
    ORDER_COMPLETED
}

