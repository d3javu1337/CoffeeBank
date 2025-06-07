import React, {FC, use, useContext, useEffect, useState} from 'react';
import Header from "../component/Header";
import ClientService from "../service/ClientService";
import {Context} from "../index";
import AccountService from "../service/AccountService";
import {observer} from "mobx-react-lite";
import {account_route} from "../routes/BaseRoutes";

const MainPage: FC = () => {

    const [state, setState] = useState({});
    const {store, clientStore, accountStore} = useContext(Context);

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
    }
    ,
    [accountStore, clientStore, store]);



    if(store.isLoading){
        return (<h2>loading...</h2>)
    }

    const createAccount = () => {
        store.setIsLoading(true)
        AccountService.createAccount().then(res => {
            try {
                AccountService.getAccount().then(accResp => {
                    accountStore.setAccount(accResp.data)
                })
            } catch (e) {
                console.error(e)
            } finally {
                store.setIsLoading(false)
            }
        })
        setState({})
    }

    if (!accountStore.Account){
        return (
            <div>
                <p>you does not have an account? Wanna create?</p>
                <input type={'submit'} value={'create'} onClick={() => {
                    createAccount()
                }}/>
            </div>
        )
    }

    return (
        <div style={{height:'100%', display: 'flex', flexDirection: 'column'}}>
            <div style={{width:'30%', height:'50%', alignSelf: 'start', justifyContent: 'center', alignItems: 'center'}}>
                <h3>client data</h3>
                <div>
                    <p>{clientStore.Client.id}</p>
                    <p>{clientStore.Client.surname + ' ' + clientStore.Client.name}</p>
                </div>
                <h3>account</h3>
                <div>
                    <a key={accountStore.Account.id} href={account_route}>
                        <div style={{border:'1px solid black'}}>
                            <p>{accountStore.Account.id}</p>
                            <p>{accountStore.Account.accountName}</p>
                            <p>{accountStore.Account.accountDeposit}</p>
                            <p>{accountStore.Account.accountType}</p>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    );
};

export default observer(MainPage);