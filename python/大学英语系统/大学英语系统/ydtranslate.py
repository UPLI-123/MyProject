import urllib.request
import urllib.parse
import json


def translate(str):
    if not str:
        return
    url = "http://fanyi.youdao.com/translate?smartresult=dict&smartresult=rule"
    data = {
        'from': 'AUTO',
        'to': 'AUTO',
        'smartresult': 'dict',
        'client': 'fanyideskweb',
        'salt': '15972856501663',
        'sign': 'a2a351e89c9492415f1f2554033cfd87',
        'lts': '1597285650166',
        'bv': '42160534cfa82a6884077598362bbc9d',
        'doctype': 'json',
        'version': '2.1',
        'keyfrom': 'fanyi.web',
        'action': 'FY_BY_CLICKBUTTION'
    }
    data['i'] = str
    data = urllib.parse.urlencode(data).encode('utf-8')
    req = urllib.request.Request(url, data)
    req.add_header('User-Agent',
                   'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36')
    response = urllib.request.urlopen(req)
    # response.add_header(header)
    # print(response.header)
    # print(req.headers)
    html = response.read().decode('utf-8')
    # print(html['translateResult'])
    dict1 = json.loads(html)
    return dict1['translateResult'][0][0]['tgt']
