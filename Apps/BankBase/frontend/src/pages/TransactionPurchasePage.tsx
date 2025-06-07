import React, {FC, useContext, useEffect, useState} from 'react';
import {NavLink, useLocation, useNavigate} from "react-router-dom";
import {observer} from "mobx-react-lite";
import {Context} from "../index";
import TransactionService from "../service/TransactionService";
import {home_route} from "../routes/BaseRoutes";
import InvoiceDto from "../dto/model/invoice/InvoiceDto";

let uuidRegex = RegExp('[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}')


const TransactionPurchasePage: FC = () => {

    const fullPath = useLocation();
    const path =  fullPath.pathname;
    const splittedPath = path?.split("/");
    const uuid = splittedPath[3];

    const nav = useNavigate();

    const {store} = useContext(Context);

    const [invoiceId, setInvoiceId] = useState('');

    const [invoice, setInvoice] = useState({} as InvoiceDto);

    useEffect(() => {

        if(!uuidRegex.test(uuid)) {
            nav('/main')
        }
        if (invoiceId !== uuid) {
            setInvoiceId(uuid)
        }
        TransactionService.getInvoiceInfo(uuid).then((res) => {
            setInvoice(res.data);
        })
    }, []);

    return (
        <div>
            <div>
                <input value={invoiceId} style={{width:'50%'}} disabled={true}/><br/>
                <p>{invoice.amount} rub</p>
                <input type={'submit'} value={'purchase'} onClick={(e) => TransactionService.purchase(uuid)}/>
            </div>
        </div>
    );
};

export default observer(TransactionPurchasePage);