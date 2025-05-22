import {
    account_route,
    auth_route,
    card_route,
    home_route,
    login_route,
    main_route,
    registration_route
} from "./BaseRoutes";
import MainPage from "../pages/MainPage";
import AccountPage from "../pages/AccountPage";
import CardPage from "../pages/CardPage";
import HomePage from "../pages/HomePage";
import AuthPage from "../pages/AuthPage";

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
        path: card_route,
        Component: CardPage
    }
]

export const publicRoutes = [
    {
        path: home_route,
        Component: HomePage
    },
    {
        // path: login_route,
        path: auth_route,
        Component: AuthPage
    },
    {
        // path: registration_route,
        path: auth_route,
        Component: AuthPage
    }
];