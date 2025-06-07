import {AxiosResponse} from "axios";
import {CardCompact} from "../dto/model/card/CardCompactDto";
import api from "../http";
import CardRequest from "../dto/requests/card/CardRequest";
import {AccountIdRequest} from "../dto/requests/account/AccountIdRequest";
import {CardType} from "../dto/model/card/CardType";
import CardCreateRequest from "../dto/requests/card/CardCreateRequest";
import CardRenameRequest from "../dto/requests/card/CardRenameRequest";


export default class CardService {
    static async getCards(): Promise<AxiosResponse<Array<CardCompact>>> {
        return api.get<CardCompact[]>('/card');
    }

    static async getCard(cardId: number): Promise<AxiosResponse<CardCompact>> {
        return api.get<CardCompact>('/card', { params : { cardId: cardId } })
    }

    static async createCard(type: CardType): Promise<AxiosResponse<void>> {
        const data = new CardCreateRequest(type);
        return api.post<void>('/card', data);
    }

    static async renameCard(accountId: number, cardId: number, newName: string): Promise<AxiosResponse<void>> {
        const data = new CardRenameRequest(accountId, cardId, newName);
        return api.patch<void>('/card', data);
    }
}