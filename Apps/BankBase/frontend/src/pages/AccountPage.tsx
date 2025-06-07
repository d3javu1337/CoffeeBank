import React, {FC, useContext, useEffect, useState} from 'react';
import {Context} from "../index";
import {NavLink} from "react-router-dom";
import AccountService from "../service/AccountService";
import CardService from "../service/CardService";
import {observer} from "mobx-react-lite";
import {CardType} from "../dto/model/card/CardType";

const AccountPage: FC = () => {

    const {store, clientStore, accountStore, cardStore} = useContext(Context);

    const [newCardType, setNewCardType] = useState<CardType>();


    useEffect(() => {

        CardService.getCards()
            .then(res => {
                store.setIsLoading(true)
                try {
                    cardStore.setCards(res.data || {});
                } catch (e) {
                    console.error(e)
                } finally {
                    store.setIsLoading(false)
                }
            });
        AccountService.getAccount()
            .then(res => {
                store.setIsLoading(true)
                try {
                    accountStore.setAccount(res.data || {})
                } catch (e) {
                    console.error(e)
                } finally {
                    store.setIsLoading(false)
                }
            })

    }, [store, clientStore, accountStore, cardStore]);

    return (
            <div>
                <div>
                    {accountStore.Account.id}
                </div>
                {cardStore.cards.length > 0 &&
                <div>
                    {Array.isArray(cardStore.Cards) && cardStore.Cards.map(card =>
                        <a href={'/account/card/' + card.id} key={card.id}>
                            <div className="card" style={{border: '1px solid black'}}>
                                <p>{card.name}</p>
                                <p>{card.type}</p>
                                <p>{card.number}</p>
                            </div>
                        </a>
                    )}
                </div>
                }
                {cardStore.cards.length === 0 &&
                    <div>
                        <p>you does not have any cards. Wanna create some?</p>
                        <input type={'submit'} value={'create'} onClick={() => CardService.createCard(newCardType || CardType.debit)}/>
                    </div>
                }
                <div style={{border: '1px solid black'}}>
                    <NavLink to={'/transaction/transfer'}>
                        <button>transfer</button>
                    </NavLink>
                </div>
            </div>
    );
};

export default observer(AccountPage);