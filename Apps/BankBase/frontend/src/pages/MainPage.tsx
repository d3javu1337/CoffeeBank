import React, {FC, use, useContext, useEffect, useState} from 'react';
import Header from "../component/Header";
import ClientService from "../service/ClientService";
import {Context} from "../index";
import AccountService from "../service/AccountService";
import {observer} from "mobx-react-lite";
import {account_route} from "../routes/BaseRoutes";

const MainPage: FC = () => {

    // const getClient = async () => await ClientService.getClientInfo().then(e => e.data)
    // const [state, setState] = useState([]);
    const {store, clientStore, accountStore} = useContext(Context);
    // const {clientStore} = useContext(Context);
    useEffect(() => {
    ClientService.getClientInfo().then(res => {
            store.setIsLoading(true)
            try {
                clientStore.setClient(res.data)
            } catch (e) {
                console.error(e)
            } finally {
                store.setIsLoading(false)
            }
        })

    AccountService.getAccounts().then(res => {
            store.setIsLoading(true)
            try {
                const t = res.data
                // res.data.forEach(console.log)
                accountStore.setAccounts(t)
            } catch (e) {
                console.error(e)
            } finally {
                store.setIsLoading(false)
            }
        })
    }
    ,
    [accountStore, clientStore, store]);



    if(store.isLoading){
        return (<h2>loading...</h2>)
    }

    return (
        <div style={{height:'100%', display: 'flex', flexDirection: 'column'}}>
            <Header/>
            <div style={{width:'30%', height:'50%', alignSelf: 'start', justifyContent: 'center', alignItems: 'center'}}>
                <h3>client data</h3>
                <div>
                    <p>{clientStore.Client.id}</p>
                </div>
                <h3>accounts</h3>
                <div>
                    <div>{Array.isArray(accountStore.Accounts) &&
                        accountStore.Accounts.map(acc => <a key={acc.id} href={account_route + "/" + acc.id}>
                            <div style={{border:'1px solid black'}}>
                                <p>{acc.id}</p>
                                <p>{acc.accountName}</p>
                                <p>{acc.accountDeposit}</p>
                                <p>{acc.accountType}</p>
                            </div>
                        </a>)}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default observer(MainPage);