import React, {FC} from 'react';
import {observer} from "mobx-react-lite";

const Card: FC = () => {
    return (
        <div style={{width:'100px', height:'50px',border:'1px solid black'}}>
        </div>
    );
};

export default observer(Card);