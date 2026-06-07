// pages/DemoPizzaPage.tsx
import {FC, useEffect, useState} from 'react';

interface DemoPizzaPageProps {
    apiHost: string;
    token: string
}

const DemoPizzaPage: FC<DemoPizzaPageProps> = ({apiHost, token}) => {
    const [htmlContent, setHtmlContent] = useState('');

    useEffect(() => {
        fetch('/pizzaDemo.html')
            .then(res => res.text())
            .then(html => {
                const filledHtml = html
                    .replace('__API_URL__', `${apiHost}/invoice`)
                    .replace('__INVOICE_TOKEN__', token)
                setHtmlContent(filledHtml);
            });
    }, []);

    if (!htmlContent) return <div>Загрузка...</div>;

    return (
        <iframe
            srcDoc={htmlContent}
            style={{
                width: '100%',
                height: '100vh',
                border: 'none',
                position: 'fixed',
                top: 0,
                left: 0
            }}
            title="Pizza Shop"
        />
    );
}

export default DemoPizzaPage;