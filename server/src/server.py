#Networking
import socketserver
import socket
import threading
import ctypes

#UI
import tkinter as tk

#windows keypress
from platform import system
try:
    import win32api
    import win32con
finally:
    pass #Do nothing if not windows machine

#Define constants
HOST = str(socket.gethostbyname(socket.gethostname())) #For local testing use '127.0.0.1'
PORT = 12345

#Define color constants
COLOR_OK = "#33AA33"
COLOR_BAD = "#AA3333"

#Find out current os
OS = system()

#TODO: remove global usage of variable app (instance of GUIapplication)
#TODO: check for win32api amd win32con before sendkey

def send_key(keycode, pressed):
    if OS == 'Windows':
        if pressed:
            win32api.keybd_event(keycode, 0, win32con.KEYEVENTF_EXTENDEDKEY, 0)
        else:
            win32api.keybd_event(keycode, 0, win32con.KEYEVENTF_KEYUP, 0)
    else:
        app.debugg_write("can't send keys on {}".format(OS))

class TCPHandler(socketserver.BaseRequestHandler):
    """This class handles TCP requests and receives keypresses"""
    def handle(self):
        #Data is send as c_byte value
        self.alive = bool(ord(self.request.recv(1)))
        if self.alive == True:
            self.data = ord(self.request.recv(1))
            app.debugg_write("{} sent: {}".format(self.client_address[0], str(self.data)))
            send_key(self.data, True)
            
        self.request.sendall(ctypes.c_bool(True)) #Send Keepalive ping
class ThreadedTCPServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    """Overwrite default tcp server's methods with thread friendly variants"""
    pass

class GUIapplication(tk.Frame):
    def __init__(self, master=None):
        tk.Frame.__init__(self, master)
        self.pack()
        self.create_widgets()
    def create_widgets(self):
        #Create initial widgets

        statusFrame = tk.LabelFrame(self, text="server info")
        statusFrame.pack(side= tk.TOP)

        debuggFrame  = tk.LabelFrame(self, text="Server messages:")
        debuggFrame.pack(side = tk.BOTTOM)
        
        self.displayIPaddr = tk.Label(statusFrame, text=HOST)
        self.displayIPaddr.pack(side = tk.TOP)
        
        port = "PORT: "+str(PORT);
        self.displayPortNr = tk.Label(statusFrame, text=port)
        self.displayPortNr.pack(side = tk.TOP, expand=1)


        self.status = tk.StringVar()
        self.statusColor = COLOR_BAD
        self.status.set("OFFLINE")
        
        self.displayStatus = tk.Label(statusFrame, textvariable=self.status, fg=self.statusColor)
        self.displayStatus.pack(side = tk.BOTTOM)

        self.toggleServer = tk.Button(self, text="Start", command = self.toggle_server)
        self.toggleServer.pack(side= tk.BOTTOM)

        self.debuggField = tk.Text(debuggFrame, yscrollcommand=True, state=tk.DISABLED)
        self.debugg_write("The Humdinger controller app,running on {}".format(OS))
        self.debuggField.pack(side = tk.BOTTOM, fill =tk.BOTH)

    def debugg_write(self, text):
        #Write debugg text inside tkinter instead of console
        self.debuggField.config(state=tk.NORMAL)
        self.debuggField.insert(tk.INSERT, text+"\n")
        self.debuggField.config(state=tk.DISABLED)
        
    def toggle_server(self):
        if self.status.get() == "OFFLINE":
            self.status.set("ONLINE")
            self.statusColor = COLOR_OK
            toggleText = "Stop"
            self.start_server()
        else:
            self.status.set("OFFLINE")
            self.statusColor = COLOR_BAD
            toggleText = "Start"
            self.stop_server()
            
        self.toggleServer.config(text=toggleText)
        self.displayStatus.config(fg=self.statusColor)
        
    def start_server(self):

        #Start threaded server
        self.server = ThreadedTCPServer((HOST, PORT), TCPHandler)
        self.server_thread = threading.Thread(target=self.server.serve_forever)
        
        #Make server thread exit when main thread terminates
        self.server_thread.daemon = True
        self.server_thread.start()

        self.debugg_write("TCP/IP Server on PORT: {} with IP : {} has  been successfully started".format(PORT, HOST))

    def stop_server(self):
        self.server.shutdown()
        self.server.server_close()
        
        self.debugg_write("TCP/IP server has been successfully closed")
       

if __name__ == "__main__":
    app = GUIapplication()
    app.master.title('The Humdinger Server')
    app.master.minsize(width=120, height=120)
    app.master.resizable(0,0)
    app.mainloop()


    
    