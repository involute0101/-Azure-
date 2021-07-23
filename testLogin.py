import os
import time
from flask import Flask
app = Flask(__name__)
@app.route('/getCode')
def getCode():
    os.system("nohup az login&")
    time.sleep(1)
    with open("nohup.out", 'r') as f:  # 打开文件
        lines = f.readlines()  # 读取所有行 # 取第一行
        last_line = lines[-1]  # 取最后一行
        begin=last_line.index("code")+5
    code=last_line[begin:begin+9]
    return {"code":code}
if __name__ == '__main__':
    app.run(host="0.0.0.0", port="33")

