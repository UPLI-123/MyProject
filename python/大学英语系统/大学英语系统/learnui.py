import tkinter as tk
import sqlite3
from tkinter import messagebox
import pyttsx3
import dayinEnglish

# 创建一个界面类
class learn_ui():
    def __init__(self,root,name):
        self.root = root
        self.name = name
        # 获取当前所选则的单词书
        with sqlite3.connect("User.db") as coon:
            cur = coon.cursor()
            sql = "select bname from book where version =1"
            cur.execute(sql)
            book_name = cur.fetchall()
            pass
        if not book_name:
            self.book_name = '无'
        else:
            self.book_name = book_name[0][0]
        if self.name == 'admin':
            self.count  = 0
            pass
        else:
            if self.book_name!='无':
                with sqlite3.connect("{}.db".format(self.name)) as coon:
                    cur = coon.cursor()
                    sql = "select nowlength from temp where bookname = '{}'".format(self.book_name)
                    cur.execute(sql)
                    self.count = cur.fetchone()[0]
                    print(self.count)
            pass
        # self.count = 0
        self.display()
        # print(1)
        pass
    # todo 用来更新数据库 中当前用户正在学习的地方
    def update_info_db(self):
        if self.name == 'admin':
            return
            pass
        else:
            # 更新操作
            with sqlite3.connect('{}.db'.format(self.name)) as coon:
                cur  = coon.cursor()
                sql = "update temp set nowlength = {} where bookname = '{}'".format(self.count,self.book_name)
                cur.execute(sql)
            pass
        pass

    # todo 下一页
    def method_next_page(self):
        if not self.book_list:
            tk.messagebox.showinfo("提示", "请先选择了单词书，再来进行学习")
            return
        self.count = self.count + 1
        if self.count  == len(self.book_list):
            self.count =0
            pass
        self.update_info_db()
        # 跟新标签
        self.lable_word.configure(text = self.book_list[self.count][0])
        self.count_lab.configure(text = str(self.count+1)+"/"+str(self.count_book))
        self.var_write.set("")
        # 清空
        self.text_means.delete(0.0,tk.END)
        pass
    # todo 上一页
    def method_pro_page(self):
        if not self.book_list:
            tk.messagebox.showinfo("提示", "请先选择了单词书，再来进行学习")
            return
        if self.count ==  0 :
            return
        self.count = self.count-1
        self.update_info_db()

        self.lable_word.configure(text = self.book_list[self.count][0])

        self.count_lab.configure(text=str(self.count + 1) + "/" + str(self.count_book))
        self.var_write.set("")
        self.text_means.delete(0.0,tk.END)
        pass
    # todo 我不认识
    def method_dont_know(self):
        if self.book_name=='无':
            tk.messagebox.showinfo("提示","请先选择了单词书，再来进行学习")
            return
        self.text_means.delete(0.0,tk.END)
        self.text_means.insert(tk.END,self.book_list[self.count][1])
        pass
    # todo 发音
    def metho_nounce(self):
        # print("1111111111111")
        if self.book_name == '无':
            tk.messagebox.showinfo("提示","请选择好了单词书，再来进行学习")
            return
            pass
        engine = pyttsx3.init()
        engine.say(self.book_list[self.count][0])
        engine.runAndWait()
        pass
    # todo 加入单词本
    def metho_add_words(self):
        # 加入单词本操作
        if self.name == "admin":
            tk.messagebox.showinfo("提示","当前用户不支持单词本功能的使用")
            return
            pass
        if self.book_name == '无':
            tk.messagebox.showinfo("提示","请先选择了单词书再来进行此功能的测试")
            return

        # 首先先查看 一下单词本中 是否包含该单词
        with sqlite3.connect("{}.db".format(self.name)) as coon:
            cur = coon.cursor()
            sql = "select word,means from words"
            cur.execute(sql)
            wordlist = cur.fetchall()
            pass
        tup_word = tuple(self.book_list[self.count])
        print(tup_word)
        print(wordlist)
        if tup_word in wordlist:
            tk.messagebox.showinfo("提示","该单词已经在你的数据库中了")
            return
        else:
        # 加入单词本操作
            with sqlite3.connect("{}.db".format(self.name)) as coon:
                cur = coon.cursor()
                sql = "insert into words values(null,'{}','{}',0)".format(self.book_list[self.count][0],self.book_list[self.count][1])
                cur.execute(sql)
                pass

        tk.messagebox.showinfo("提示","添加成功")
        pass
    # 设计布局
    def display(self):
        # 首先题目为 我们一起背单词吧
        self.title = tk.Label(self.root,text = "我们一起背单词吧!!",font =('微软雅黑',15,"bold",'italic'))
        self.title.place(relx=0.0,rely=0.0,relwidth =1 , relheight = 0.05)
        # 使用一个框架来作为整体的布局
        self.top_frame = tk.Frame(self.root,bg='#e0f6ff')
        self.top_frame.place(relx=0.0,rely=0.05,relwidth =1 , relheight = 0.95)
        # 第一个所被单词书的名称
        # 用个标签来存储当前所选单词书的名称
        self.lab_book_name = tk.Label(self.top_frame,text = self.book_name+"单词书学习",font = ('宋体',20,"bold",'italic'),bd = 0,bg='#e0f6ff',anchor ="w")
        self.lab_book_name.place(relx=0.05,rely=0.0,relwidth =1 , relheight = 0.1)
        if self.book_name != '无':
            with sqlite3.connect("User.db") as coon:
                cur = coon.cursor()
                sql = "select *from {}".format(self.book_name)
                cur.execute(sql)
                book_list = cur.fetchall()
                pass
        else:
            book_list = []
            pass
            # print(book_list)
        if book_list:
            self.book_list = book_list
        else:
            self.book_list = []

            # print(self.book_list)
            pass
        # 获取单词的数量
        self.count_book =len(book_list)
        self.count_lab = tk.Label(self.top_frame,text = str(self.count+1)+"/"+str(self.count_book),font =('微软雅黑',15),bg ='#e0f6ff' ,bd =0,anchor ="w")
        self.count_lab.place(relx=0.10,rely=0.1,relwidth =1 , relheight = 0.05)

        self.word_learn_frame = tk.Frame(self.top_frame,bd =20,bg ="#f5f5f5")
        self.word_learn_frame.place(relx=0.05,rely=0.15,relwidth =0.9 , relheight = 0.7)
        # if not self.book_list:
        #     # 单词标签
        #     self.lable_word = tk.Label(self.word_learn_frame, text='admin', font=('微软雅黑', 24))
        #     self.lable_word.place(relx=0.0, rely=0.0, relwidth=1, relheight=0.3)
        # else:
        # 单词标签
        self.lable_word = tk.Label(self.word_learn_frame, text=self.book_list[self.count][0], font=('微软雅黑', 24,"bold"),bg ="#f5f5f5",fg ="#3b93ee",anchor = 'w')
        self.lable_word.place(relx=0.05, rely=0.0, relwidth=0.25, relheight=0.1)
        # 发声按钮
        self.img_nocie = tk.PhotoImage(file ="image/4_02.gif")

        self.but_voice = tk.Button(self.word_learn_frame , image = self.img_nocie,command = self.metho_nounce,bd =0,bg = "#f5f5f5")
        self.but_voice.place(relx=0.3, rely=0.0, relwidth=0.05, relheight=0.1)
        # 用一个标签存放解释
        self.lab_cub = tk.Label(self.word_learn_frame,text = '解 释：',font=("宋体",22),bd =0,bg = "#f5f5f5",anchor = 'w',fg="#acacac")
        self.lab_cub.place(relx=0.05, rely=0.1, relwidth=1, relheight=0.1)

        self.text_means = tk.Text(self.word_learn_frame,font = ('宋体',24))
        self.text_means.place(relx=0.15,rely=0.2,relwidth =0.7 , relheight = 0.6)

        # 接下来一个 你可以选择临摹一下这个单词
        self.lab_write = tk.Label(self.word_learn_frame,text = "您还可以临摹下单词:",font =('宋体',20),fg = "#acacac",bg = "#f5f5f5",bd =0,anchor ='w')
        self.lab_write.place(relx=0.05, rely=0.85, relwidth=0.4, relheight=0.1)

        # 设置一下 一个文本框用来记录所描写的单词
        self.var_write = tk.StringVar()
        self.var_write.set("")
        self.en_wirte = tk.Entry(self.word_learn_frame,font =('宋体',20),fg = "#3b93ee",bg = "#f5f5f5",bd =2,relief = "sunken",textvariable = self.var_write)
        self.en_wirte.place(relx=0.45, rely=0.85, relwidth=0.3, relheight=0.1)

        self.img_up = tk.PhotoImage(file = "image/4_03.gif")
        self.img_down = tk.PhotoImage(file ="image/4_04.gif")

        self.but_up = tk.Button(self.top_frame,image = self.img_up,command = self.method_pro_page,bd = 0,bg ="#e0f6ff" )
        self.but_up.place(relx=0.0,rely=0.0,relwidth =0.05, relheight = 1)

        self.but_down = tk.Button(self.top_frame,image = self.img_down,command = self.method_next_page,bd = 0,bg ="#e0f6ff")
        self.but_down.place(relx=0.95, rely=0.0, relwidth=0.05, relheight=1)

        self.but_add_word = tk.Button(self.top_frame, text='加入单词本', font=('宋体',18),command = self.metho_add_words,fg ="#ffffff",bg="#71ce36")
        self.but_add_word.place(relx=0.65, rely=0.87, relwidth=0.2, relheight=0.10)
        # 显示 解释
        self.but_forget = tk.Button(self.top_frame,text = '我不认识' ,font =('宋体',18),command = self.method_dont_know,fg ="#ffffff",bg="#71ce36")
        self.but_forget.place(relx=0.15, rely=0.87, relwidth=0.2, relheight=0.10)
        # 事件的绑定
        self.top_frame.focus_set()
        self.top_frame.bind("<a> ",lambda event:self.method_pro_page())
        self.top_frame.bind("<d>",lambda event:self.method_next_page())
        self.top_frame.bind("<s>",lambda event:self.method_dont_know())
        self.top_frame.bind("<w>", lambda event: self.en_wirte.focus())
        self.en_wirte.bind('<Return>',lambda event: self.top_frame.focus_set())
        self.top_frame.bind("<z>",lambda event: self.metho_add_words())
        self.top_frame.bind("<e>",lambda event:self.metho_nounce())







        # 首先分为上下两个部分
        # # 上部分 也分为两部分
        # self.left_frame = tk.Frame(self.top_frame,bg='red')
        # self.left_frame.place(relx=0.0,rely=0.0,relwidth =0.7 , relheight = 1)
        #
        # self.left_page_frame = tk.Frame(self.left_frame,bg='yellow')
        # self.left_page_frame.place(relx=0.0,rely=0.0,relwidth =0.2 , relheight = 1)
        #

        #
        # self.main_word_frame = tk.Frame(self.left_frame,bg='skyblue')
        # self.main_word_frame.place(relx=0.2,rely=0.0,relwidth =0.6 , relheight = 1)
        #
        # # 接下来用一个文本框来接搜 翻译信息
        #


        #
        # self.right_page_frame = tk.Frame(self.left_frame,bg='green')
        # self.right_page_frame.place(relx=0.8,rely=0.0,relwidth =0.2 , relheight = 1)
        #

        #
        # self.right_frame = tk.Frame(self.top_frame,bg='yellow')
        # self.right_frame.place(relx=0.7,rely=0.0,relwidth =0.3, relheight = 1)
        #
        # self.book_info_frame = tk.Frame(self.right_frame,bg='blue')
        # self.book_info_frame.place(relx=0.0,rely=0.0,relwidth =1 , relheight = 0.3)
        #
        # self.lab_book_name = tk.Label(self.book_info_frame,text = '所学单词书:',font = ('宋体',22))
        # self.lab_book_name.place(relx=0.05,rely=0.05,relwidth =0.7 , relheight = 0.3)
        #
        #
        # self.lab_book_info_name = tk.Label(self.book_info_frame,text = self.book_name,font = ('微软雅黑',24),fg = 'red')
        # self.lab_book_info_name.place(relx=0.15, rely=0.45, relwidth=0.8, relheight=0.3)
        # self.book_op_frame = tk.Frame(self.right_frame,bg = 'red')
        # self.book_op_frame.place(relx=0.0,rely=0.3,relwidth =1 , relheight = 0.7)
        # # 两个按钮 、发音 按钮 和 加入单词本的按钮

        #


        # # 下半部分操作
        # self.buttom_frame = tk.LabelFrame(self.root,bg='blue',text = '每日一句',font =('宋体',24))
        # self.buttom_frame.place(relx = 0.0,rely =0.7,relwidth = 1 ,relheight=0.3)
        #
        # # 获得 句子填写到相应的模块
        #
        # word_tuple = dayinEnglish.find_word()
        # # print(word_tuple)
        # word = word_tuple[0]
        # mean = word_tuple[1]
        # print(word)
        # # print(mean)
        #
        # self.lab_words = tk.Label(self.buttom_frame,text = word , font = ('宋体',15), wraplength=900)
        # self.lab_words.place(relx = 0.05,rely =0.05,relwidth = 0.95 ,relheight=0.3)
        #
        # self.lab_means = tk.Label(self.buttom_frame,text = mean ,font =("宋体",15),wraplength=900)
        # self.lab_means.place(relx=0.05, rely=0.45, relwidth=0.95, relheight=0.3)
        pass
    pass