import React, {FC, useContext, useEffect} from 'react';
import {Context} from "../index";
import {useLocation} from "react-router-dom";
import AccountService from "../service/AccountService";
import CardService from "../service/CardService";
import {observer} from "mobx-react-lite";

const AccountPage: FC = () => {

    const {store, clientStore, accountStore, cardStore} = useContext(Context);
    const path = useLocation();
    const from = path.pathname;
    const accountId = Number.parseInt(from?.split('/')[2]);


    useEffect(() => {

        CardService.getCards(accountId)
            .then(res => {
                store.setIsLoading(true)
                try {
                    cardStore.setCards(res.data);
                } catch (e) {
                    console.error(e)
                } finally {
                    store.setIsLoading(false)
                }
            })

    }, [store, clientStore, accountStore]);

    return (
        <div>
            <p>{accountId}</p>
            {Array.isArray(cardStore.Cards) && cardStore.Cards.map(card =>
                <a href={from+'/card/'+card.id} key={card.id}>
                    <div className="card" style={{border: '1px solid black'}}>
                        <p>{card.name}</p>
                        <p>{card.type}</p>
                        <p>{card.number}</p>
                    </div>
                </a>
            )}
        </div>
    );
};

export default observer(AccountPage);