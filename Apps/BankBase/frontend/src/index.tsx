import React, {createContext} from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import Store from "./store/store";
import ClientStore from "./store/ClientStore";
import AccountStore from "./store/AccountStore";
import CardStore from "./store/CardStore";

const store = new Store();
const clientStore = new ClientStore();
const accountStore = new AccountStore();
const cardStore = new CardStore();

export const Context = createContext({
    store, clientStore, accountStore, cardStore
})

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  // <React.StrictMode>
    <Context.Provider value={{store, clientStore, accountStore, cardStore}}>
        <App />
    </Context.Provider>
  // </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
