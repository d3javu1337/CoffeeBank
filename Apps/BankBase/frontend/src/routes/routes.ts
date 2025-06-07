import {
    account_route,
    auth_route,
    card_route,
    home_route,
    main_route, purchase_route,
    transfer_route
} from "./BaseRoutes";
import MainPage from "../pages/MainPage";
import AccountPage from "../pages/AccountPage";
import CardPage from "../pages/CardPage";
import HomePage from "../pages/HomePage";
import AuthPage from "../pages/AuthPage";
import TransactionTransferPage from "../pages/TransactionTransferPage";
import TransactionPurchasePage from "../pages/TransactionPurchasePage";

export const authenticatedRoutes = [
    {
        path: main_route,
        Component: MainPage
    },
    {
        path: account_route,
        Component: AccountPage
    },
    {
        path: account_route + card_route + '/:cardId',
        Component: CardPage
    },
    {
        path: transfer_route,
        Component: TransactionTransferPage
    },
    {
        path: purchase_route + '/:invoiceId',
        Component: TransactionPurchasePage
    },
]

export const publicRoutes = [
    {
        path: home_route,
        Component: HomePage
    },
    {
        path: auth_route,
        Component: AuthPage
    },
    {
        path: auth_route,
        Component: AuthPage
    }
];