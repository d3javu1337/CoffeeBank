import React, {FC, useContext} from 'react';
import {Context} from "../index";
import {useLocation} from "react-router-dom";

const CardPage: FC = () => {
    const {store, clientStore, accountStore, cardStore} = useContext(Context);
    const path = useLocation();
    const from = path.pathname;
    const splittedPath = from?.split('/')[2];
    const accountId = Number(splittedPath[splittedPath.length - 3]);
    const cardId = Number(splittedPath[splittedPath.length - 1]);
    return (
        <div>
            <p>cardPage</p>
        </div>
    );
};

export default CardPage;