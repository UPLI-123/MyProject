import tkinter as tk
import tkinter.messagebox
import sqlite3
import os
import mainui

# TODO 在画布上进行操作实现 帮助文档的出现

# 获得数据库连接
def get_Conn():
    # 获得数据库连接
    try:
        # 如果数据库不存在的话就创建数据库
        conn = sqlite3.connect("User.db")
        return conn
        pass
    except Exception:
        raise( "数据库连接失败")
        pass
    pass
# 关闭数据连接
def close_Conn(conn):
    conn.close()

# 查询数据库中的数据
def select_DB(username):
    with sqlite3.connect("User.db") as conn:
        # 游标
        cursor = conn.cursor()
        # sql 语句
        sql = "select * from login where username = "+"'"+username+"'"
        # 执行
        cursor.execute(sql)
        # 获得查询的返回值
        login_user = cursor.fetchall()
        pass
    return login_user
    pass

# 定义登录界面
class login_ui():
    login_name = 'admin'
    def __init__(self):
        # 创建登录边框
        self.root = tk.Tk()
        # 初始化数据库
        self.inint_DB()
        # 对界面进行设计
        self.root = self.display(self.root)
        self.root.mainloop()


    # 界面设计界面
    def display(self,top=None):
        # 创建两个变量，分别和username 和 password 绑定到一起。
        self.var_username = tk.StringVar("")
        self.var_password = tk.StringVar("")
        # 设置边框的大小
        top.geometry("800x500+300+100")
        # 设置边框为不可变
        top.resizable(False, False)
        # 设置边框标题
        top.title("登录界面")
        # 左右  两个部分 一个 是登陆部分 ， 一个是提示部分
        self.right_frame= tk.Frame(top,bg = '#f6f6f6')
        self.right_frame.place(relx=0.0, rely=0.0, relheight=1, relwidth=0.5)
        # 左边部分的布局

        self.img_learn = tk.PhotoImage(file = "image/8_01.gif")


        # 在登录界面创建一个带有图片的按钮
        but_img = tk.Button(self.right_frame,bg='#f6f6f6',command=self.foucs_window,cursor="mouse",image = self.img_learn)
        # 将画布显示出来
        but_img.place(relx=0.2, rely=0.2, relheight=0.3, relwidth=0.6)

        # 下面是两个标签
        self.lab_learn =  tk.Label(self.right_frame,text = "坚持学习",bg ="#f6f6f6",fg="#333",font =('微软雅黑',20,'bold'))
        self.lab_bottom_learn = tk.Label(self.right_frame,text = "享受学习的快乐",bg ="#f6f6f6",fg="#666",font =('微软雅黑',16))
        # 然后将两个标签显示出来
        self.lab_learn.place(relx=0.2, rely=0.6, relheight=0.1, relwidth=0.6)
        self.lab_bottom_learn.place(relx=0.2, rely=0.7, relheight=0.1, relwidth=0.6)

        self.left_frame = tk.Frame(top,bg ='#fdfdfd')
        self.left_frame.place(relx=0.5, rely=0.0, relheight=1, relwidth=0.5)

        # 一个标签 用来 欢迎
        self.lab_welcome = tk.Label(self.left_frame,text = '欢迎到来！',font = ("微软雅黑",24,"bold"),bg = "#fdfdfd")
        self.lab_welcome.place(relx=0.05, rely=0.05, relheight=0.2, relwidth=0.95)

        # 设置用户名 和 文本框
        # self.lb_username = tk.Label(self.left_frame, text='用户名：', font=("宋体", 22))
        # self.lb_username.place(relx=0.15, rely=0.533, relheight=92, relwidth=193)
        self.user_frame = tk.Frame(self.left_frame,bg = "#e8f0fe")
        self.user_frame.place(relx=0.1, rely=0.3, relheight=0.1, relwidth=0.8)
        self.img_user = tk.PhotoImage(file = "image/7_01.gif")
        self.lab_usename = tk.Label(self.user_frame,image = self.img_user,bg = "#e8f0fe")
        self.lab_usename.place(relx=0.05, rely=0.0, relheight=1, relwidth=0.1)
        self.e_username = tk.Entry(self.user_frame, font=("宋体", 22), textvariable=self.var_username,bg = "#e8f0fe",bd =0)
        self.e_username.place(relx=0.15, rely=0.0, relheight=1, relwidth=0.85)
        self.pad_frame = tk.Frame(self.left_frame,bg = "#e8f0fe")
        self.pad_frame.place(relx=0.1, rely=0.5, relheight=0.1, relwidth=0.8)
        # 首先在光标固定在用户名文本框
        self.e_username.focus()
        # 设置密码 和 文本框
        # self.lb_password = tk.Label(self.left_frame, font=("宋体", 22), text="密 码：")
        # self.lb_password.place(relx=0.15, rely=0.733, height=68, width=205)
        self.pad_image = tk.PhotoImage(file = "image/7_02.gif")
        self.lab_pad = tk.Label(self.pad_frame,image = self.pad_image,bg = "#e8f0fe")
        self.lab_pad.place(relx=0.05, rely=0.0, relheight=1, relwidth=0.1)
        self.e_password = tk.Entry(self.pad_frame, font=("宋体", 22), textvariable=self.var_password, show="*",bg = "#e8f0fe",bd =0)
        self.e_password.place(relx=0.15, rely=0.0, relheight=1, relwidth=0.85)
        # 创建登录按钮和重置按钮
        self.but_login = tk.Button(self.left_frame, text='登录', command=self.met_login,cursor="mouse",fg = "#fff",bg ="#bbe1d9",font = ('宋体',20,"bold"))
        self.but_login.place(relx=0.2, rely=0.7, relheight=0.1, relwidth=0.2)
        self.but_reset = tk.Button(self.left_frame, text='重置', command=self.met_reset,cursor="mouse",fg = "#fff",bg ="#bbe1d9",font = ('宋体',20,"bold"))
        self.but_reset.place(relx=0.6, rely=0.7, relheight=0.1, relwidth=0.2)
        # 将登录和回车进行绑定
        top.bind("<Return>",func=lambda event:self.met_login())
        return top
        pass
    # 登录方法按钮
    def met_login(self):
        # 先进行判断用户名和密码是否为空
        # 获取用户名
        username = self.var_username.get()
        # 对用户名进行判断
        if username == "" or (not username):
            tk.messagebox.showwarning("警告", "用户名不能为空")
            # 将光标定义在用户名的文本框中
            self.e_username.focus()
            return
            pass
        # 获取密码
        password = self.var_password.get()
        # 对密码的格式进行判断
        if password == "" or (not password):
            tk.messagebox.showwarning("警告", "密码不能为空")
            # 将光标定义在密码的文本框中
            self.e_password.focus()
            return
        # 用户名格式的判断
        if len(username)>10 or len(username)<3:
            tk.messagebox.showwarning("提示","请输入正确的用户名")
            # 清空用户框
            self.var_username.set("")
            # 固定光标
            self.e_username.focus()
            return
            pass
        # 密码的格式判断
        if len(password)<6 or len(password) >18:
            tk.messagebox.showwarning("提示","密码不符合规范")
            # 清空密码栏
            self.var_password.set("")
            # 定义光标
            self.e_password.focus()
            return
            pass
        # 从数据库中获得数据
        user = select_DB(username)
        print(user)
        # 判断用户名是否存在
        if not user:
            tk.messagebox.showinfo("提示","用户名不存在")
            self.var_username.set("")
            self.e_username.focus()
            return
        # 判断密码是否正确
        if user[0][-1] != password:
            tk.messagebox.showinfo("提示","密码不正确")
            self.var_password.set("")
            self.e_password.focus()
            return
        # 密码和账号都争正确，进行登录
        # 更改类的类变量
        login_ui.login_name = username
        tk.messagebox.showinfo("成功","登录成功")
        # 每次 都让学习的英语书为空
        with sqlite3.connect("User.db") as coon:
            cur = coon.cursor()
            sql = "update book set version = 0"
            cur.execute(sql)
            pass
        self.clos_UI()
        # 跳转到新的界面
        mainui.main_ui(login_ui.login_name)
        pass

    # 重置按钮方法
    def met_reset(self):
        self.var_username.set("")
        self.var_password.set("")
        tk.messagebox.showinfo(message="重置成功")
        # 将光标重新定位到用户名的文本框
        self.e_username.focus()
        pass
    # 初始化数据库操作
    def inint_DB(self):
        # 判断该文件是否存在
        if not os.path.exists("User.db"):
            # 获取数据库连接
            conn  = get_Conn()
            # 获得游标
            cursor = conn.cursor()
            # 准备sql 语句(用于创建一个记录用户和密码的登录表)
            sql ="create table login(username TEXT primary key,password TEXT)"
            # 执行sql 语句
            cursor.execute(sql)
            # 创建后，同时插入一条数据。
            sql01 = "insert into login values ('admin','123456')"
            # 执行
            cursor.execute(sql01)
            # 同时创建一个单词书的表格，用于存储单词书的名称和所含单词的名称
            sql02 = "create table book(bid integer PRIMARY KEY AUTOINCREMENT,bname TEXT,bwords integer,version integer)"
            # 执行
            cursor.execute(sql02)
            # 提交执行
            conn.commit()
            # 关闭游标
            cursor.close()
            # 关闭连接
            close_Conn(conn)
        pass
    # 关闭窗口
    def clos_UI(self):
        self.root.destroy()
        pass
    # 弹出注意事项
    def foucs_window(self):
        tk.messagebox.showinfo("用户须知",'''
        欢迎使用本软件，如果你是第一次使用本软件，请先使用
        用户名为admin密码为123456进行登录用于成功激活该软件
        ,在该软件的内部可以注册新的用户，也可以将原来用户的信息导入
        到该软件中去。
''')
        pass



login_window = login_ui()
