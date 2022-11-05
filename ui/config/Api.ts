import Axios from 'axios';
import getConfig from 'next/config';

// const { publicRuntimeConfig: config } = getConfig();

let urls = {
    test: 'http://localhost:8080/api', // test on kubernetes kind cluster locally
    development: 'http://localhost:8080/api', // local development
    production: 'http://localhost:8080/api', // config.ENV_API_URL + '/api', // production
}

let Api = Axios.create({
    baseURL: urls[process.env.NODE_ENV],
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

export default Api;
