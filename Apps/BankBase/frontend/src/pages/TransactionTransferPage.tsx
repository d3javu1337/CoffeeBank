import React, {FC, useContext, useEffect, useState} from 'react';
import {Context} from "../index";
import TransactionService from "../service/TransactionService";
import {observer} from "mobx-react-lite";
import AccountService from "../service/AccountService";

const TransactionTransferPage: FC = () => {
    const {store, accountStore} = useContext(Context);
    const [phoneNumber, setPhoneNumber] = useState<string>('');
    const [amount, setAmount] = useState<number>();

    useEffect(() => {
        AccountService.getAccount().then(res => {
            store.setIsLoading(true)
            try {
                const t = res.data
                accountStore.setAccount(t)
            } catch (e) {
                console.error(e)
            } finally {
                store.setIsLoading(false)
            }
        })
    }, [accountStore, store]);

    return (
        <div>
            <p>
                {accountStore.Account.accountDeposit}
            </p>
            <div>
                +7
                <input type={'tel'} value={phoneNumber} pattern={'[0-9]{0,10}'}
                       onChange={(e) => setPhoneNumber(e.target.value)}
                       required={true}/>
            </div>
            <div>
                amount
                <input type={'text'} pattern={'[0-9]*|[.]?|[,]?'} value={amount}
                       onChange={(e) => setAmount(Number.parseFloat(e.target.value))}
                       required={true}/>
            </div>
            <input type={'submit'} onClick={() => {
                if(phoneNumber && phoneNumber.match('[0-9]{10}') && amount && amount > 0 && amount <= accountStore.Account.accountDeposit) {
                    store.setIsLoading(true)
                    TransactionService.transfer(phoneNumber, amount)
                        .then(res => {
                            console.log(res.status)
                            store.setIsLoading(false)
                        })
                }
            }}/>
        </div>
    );
};

export default observer(TransactionTransferPage);