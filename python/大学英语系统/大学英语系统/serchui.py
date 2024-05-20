import tkinter as tk
import tkinter.messagebox
from tkinter import ttk
import ydtranslate
import pyttsx3

# TODO 在线搜索模块的布局设计
class search_UI():
    def __init__(self,root):
        self.root = root
        self.display()
        pass
    # todo 逐句对照
    def method_find(self,count):
        if count == 0:
            words = self.text_old.get(0.0,tk.END)
            self.text_new.configure(state = tk.NORMAL)
            self.text_new.insert(0.0,words+"\n")
            self.text_new.configure(state = tk.DISABLED)
            self.btu_obey.configure(text = '取消对照')
            self.btu_obey.configure(command = lambda :self.method_find(1))
            pass
        else:
            self.text_new.configure(state=tk.NORMAL)
            self.text_new.delete(0.0,tk.END)
            self.text_new.insert(tk.END,ydtranslate.translate(self.text_old.get(0.0,tk.END)))
            self.text_new.configure(state = tk.DISABLED)
            self.btu_obey.configure(text='逐句对照')
            self.btu_obey.configure(command=lambda: self.method_find(0))
            pass


        pass
    # todo 发音 练习
    def method_voice01(self):
        try:
            means = self.text_new.get(0.0,tk.END)
            print(means)
            void = pyttsx3.init()
            void.say(means)
            void.runAndWait()
            pass
        except Exception:
            return

        pass
    def method_search(self):
        name = self.text_old.get(0.0,tk.END)
        # 调用有道翻译的方法
        try:
            out_name = ydtranslate.translate(name)
        except Exception:
            return
        # print(out_name)
        # 启用
        self.text_new.configure(state=tk.NORMAL)
        self.text_new.insert(tk.END,out_name)
        # 停止
        self.text_new.configure(state = tk.DISABLED)
        # 同时在下面布置句子对照和发音按钮
        self.btu_trumpet01 = tk.Button(self.root, image=self.img_trumpet, bg="#fff", bd=0,command = self.method_voice01)
        self.btu_trumpet01.place(relx=0.1, rely=0.9, relheight=0.1, relwidth=0.1)
        # 逐句对照
        self.btu_obey  = tk.Button(self.root,text ='逐句对照',font=('宋体', 24,"bold"),fg = "#ffffff", command=lambda :self.method_find(0),bg ="#177bd0")
        self.btu_obey.place(relx=0.75, rely=0.9, relheight=0.1, relwidth=0.2)

        pass
    def method_reset(self):
        # 删除完全部text中的内容
        self.text_old.delete(0.0,tk.END)
        self.btu_trumpet01.destroy()
        self.btu_obey.destroy()
        # 启用
        self.text_new.configure(state=tk.NORMAL)
        self.text_new.delete(0.0,tk.END)
        # 停止
        self.text_new.configure(state=tk.DISABLED)
        pass

    # todo 发声
    def method_voice(self):
        try:
            means  = self.text_old.get(0.0,tk.END)
            print(means)
            void = pyttsx3.init()
            void.say(means)
            void.runAndWait()
            pass
        except Exception:
            return
        pass

    def display(self):
        self.text_old = tk.Text(self.root,font =('微软雅黑',22),bd =4)
        self.text_old.place(relx=0.0, rely=0.0, relheight=0.45, relwidth=1)
        self.text_old.focus()

        self.text_new = tk.Text(self.root, font=('微软雅黑', 22),state=tkinter.DISABLED,bd =4)
        self.text_new.place(relx=0.0, rely=0.55, relheight=0.45, relwidth=1)

        # 上面两个按钮 , 一个重置按钮 一个登录按钮
        self.but_search = tk.Button(self.root, text='搜 索', font=('宋体', 24),fg = "#ffffff", command=self.method_search,bg ="#177bd0")
        self.but_search.place(relx=0.75, rely=0.45, relheight=0.1, relwidth=0.2)
        # 重置按钮的实现
        self.but_reset = tk.Button(self.root, text='重 置', font=("宋体", 24,"bold"), command=self.method_reset)
        self.but_reset.place(relx=0.5, rely=0.45, relheight=0.1, relwidth=0.2)
        # 插入一个小喇叭实现语音功能

        self.img_trumpet = tk.PhotoImage(file = "image/4_01_03.gif")
        # 使用一个按钮用来接收
        self.btu_trumpet = tk.Button(self.root,image = self.img_trumpet,bg ="#fff",bd =0,command = self.method_voice)
        self.btu_trumpet.place(relx=0.1, rely=0.45, relheight=0.1, relwidth=0.1)
        # 首先选中当前的框架
        # self.root.focus_set()
        # self.but_search.bind("<Return>",lambda event:self.method_search())


        pass
    pass
