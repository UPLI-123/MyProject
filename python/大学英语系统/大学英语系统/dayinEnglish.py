import requests
from lxml import etree
# 使用 xpath 来实现 派去每日一句
def find_word():
    url = "http://dict.eudic.net/home/dailysentence"

    # 准备请求头
    headers = {
    "Cookie": "__utma=131758875.1826483123.1608134607.1608134607.1608134607.1; __utmc=131758875; __utmz=131758875.1608134607.1.1.utmcsr=link.csdn.net|utmccn=(referral)|utmcmd=referral|utmcct=/; __utmt=1; __utmb=131758875.1.10.1608134607",\
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36'
    }

    # 获取网页 的请求
    response = requests.get(url = url, headers = headers)
    # print(response.text)
    # print(response)
    # 获取 html 界面
    root = etree.HTML(response.text)
    # 获取相应的句子
    word = root.xpath('//p[@class="sect sect_en"]/text()')

    means = root.xpath('//p[@class="sect-trans"]/text()')

    return [word,means]
