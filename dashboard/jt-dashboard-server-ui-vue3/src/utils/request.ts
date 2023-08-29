import axios from 'axios'

const service = axios.create({
  baseURL: import.meta.env.VITE_BASE_API,
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 100000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  (config) => {
    // do something before request is sent
    return config
  },
  (error) => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  (response) => {
    const res = response.data

    // if the custom code is not 200, it is judged as an error.
    if (res.code !== 0) {
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  (error) => {
    console.log('err' + error) // for debug
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

export default service
