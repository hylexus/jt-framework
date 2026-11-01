/*
 * MIT License
 *
 * Copyright (c) 2017-present PanJiaChen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * Axios interceptor structure corresponds to https://github.com/PanJiaChen/vue-element-admin/blob/4.4.0/src/utils/request.js
 * Changes by hylexus: response format, timeout and application-specific error handling.
 * Comparison version 4.4.0; exact historical template import is unknown.
 */

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
