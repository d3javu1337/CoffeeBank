import api from './axios';

interface AccountData {
    id: number;
    name: string;
    deposit: number;
}

class AccountService {
    async getAccount(): Promise<AccountData> {
        const response = await api.get<AccountData>('/api/account');
        return response.data;
    }

    async createAccount(): Promise<void> {
        await api.post('/api/account');
    }
}

export default new AccountService();