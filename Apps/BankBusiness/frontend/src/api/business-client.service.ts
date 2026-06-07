import api from './axios';

interface BusinessClientData {
    officialName: string;
    brand: string;
}

class BusinessClientService {
    async getClientInfo(): Promise<BusinessClientData> {
        const response = await api.get<BusinessClientData>('/api/business-client');
        return response.data;
    }
}

export default new BusinessClientService();