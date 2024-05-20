# 从有道翻译上，派去单词的翻译，同时爬取例句
import requests
from lxml import etree
import urllib

# 准备请求头
headers = {
'Cookie': 'OUTFOX_SEARCH_USER_ID_NCOO=1988474215.123727; OUTFOX_SEARCH_USER_ID="2033388953@10.108.160.18"; _ntes_nnid=2cc60d3f92aacd4a2113611ca3d56e12,1603807191591; DICT_UGC=be3af0da19b5c5e6aa4e17bd8d90b28a|; JSESSIONID=abcTBNmgBfVsIetQ4Iczx; webDict_HdAD=%7B%22req%22%3A%22http%3A//dict.youdao.com%22%2C%22width%22%3A960%2C%22height%22%3A240%2C%22showtime%22%3A5000%2C%22fadetime%22%3A500%2C%22notShowInterval%22%3A3%2C%22notShowInDays%22%3Afalse%2C%22lastShowDate%22%3A%22Mon%20Nov%2008%202010%22%7D; search-popup-show=-1; tabRecord.examples=%23originalSound; ___rl__test__cookies=1607521584766',\
'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36'
}
# 准备请求地址
url = "http://dict.youdao.com/w/"
str = input("输入你要查询的单词的含义")
# 将字符串转化为 URL 编码的字符
url = url+str+'/'
# print(url)
# url = urllib.parse.quote(url)
# 使用xpath 来爬取数据
# 获得响应
response = requests.get(url = url ,headers = headers)
# 查看是否获得了响应
# print(response)
#  转化成 html文件
print(response.text) # 看看是否有中文乱码
root = etree.HTML(response.text)
print(root)













