import {AxiosResponse} from "axios";
import api from "../http";
import {UUID} from "node:crypto";
import InvoiceDto from "../dto/model/invoice/InvoiceDto";


export default class TransactionService {

    static async purchase(invoiceNumber: string): Promise<AxiosResponse<void>> {
        return api.post<void>('/transaction/purchase', {invoiceNumber : invoiceNumber})
    }

    static async transfer(phoneNumber: string, amount: number): Promise<AxiosResponse<void>> {
        return api.post<void>('/transaction/transfer', { phoneNumber : phoneNumber, amount : amount })
    }

    static async getInvoiceInfo(invoiceNumber: string): Promise<AxiosResponse<InvoiceDto>> {
        return api.get<InvoiceDto>('/transaction/invoiceinfo', {params : {invoiceNumber : invoiceNumber}})
    }

}