#Networking
import socketserver
import socket
import threading
import ctypes

#UI
import tkinter as tk

#windows keypress
from platform import system
if system() == "Windows":
    try:
        import win32api
        import win32con
    except ImportError:
        print("Couldn't import win32api, wind32con ")

else:
    try:
        from evdev import UInput, AbsInfo, ecodes as e 
    except ImportError:
        print("Couldn't import linux uinput")

#Define constants
HOST = "192.168.0.111"#str(socket.gethostbyname(socket.gethostname())) #For local testing use '127.0.0.1'
PORT = 21000 


"""
Enumerate keys for easier networking

Button      number  key


DPAD_LEFT   1       A
DPAD_UP     2       W
DPAD_RIGHT  3       D
DPAD_DOWN   4       S

BUT_X       5       I
BUT_A       6       O
BUT_Y       7       J
BUT_B       8       K

BUT_LB      9       Q
BUT_RB      10      E

BUT_SELECT  11      N
BUT_START   12      M
"""

#keycodes
KEY_COUNT = 12
KEYLIST_WINDOWS = [0x41, 0x57, 0x44, 0x53, 0x58,0x4F,0x59,0x42,0x51,0x45,0x4E,0x4D]
KEYLIST_LINUX = [e.KEY_A, e.KEY_W, e.KEY_D, e.KEY_S, e.KEY_I, e.KEY_O, e.KEY_J, e.KEY_K, e.KEY_Q, e.KEY_E, e.KEY_N, e.KEY_M]

#Define color constants
COLOR_OK = "#33AA33"
COLOR_BAD = "#AA3333"

#Find out current os
OS = system()

#TODO: refactor global usage of variable app (instance of GUIapplication)
#TODO: check for win32api amd win32con before sendkey

def send_key(keycode):
    if keycode < KEY_COUNT:
        if OS == 'Windows':
            send_key_windows(KEYLIST_WINDOWS[keycode])
        else:
            send_key_linux(KEYLIST_LINUX[keycode])
    else:
        app.debugg_write("Invalid keycode")
def send_key_linux(keycode):
    #Linuxi kood tuleb siia
    ui = UInput()
    ui.write(e.EV_KEY, keycode, 1)
    ui.write(e.EV_KEY, keycode, 0)
    ui.syn()
    ui.close()

def send_key_windows(keycode):
        if pressed:
            win32api.keybd_event(keycode, 0, win32con.KEYEVENTF_EXTENDEDKEY, 0)
        else:
            win32api.keybd_event(keycode, 0, win32con.KEYEVENTF_KEYUP, 0)
 

class TCPHandler(socketserver.BaseRequestHandler):
    """This class handles TCP requests and receives keypresses"""
    def handle(self):
        #Data is send as c_byte value
        app.debugg_write("New conncection from " + str(self.client_address[0]))
        while True:
            self.data = self.request.recv(1)

            if not self.data: #No data from client
                break
                app.debugg_write("No data received from: ", self.client_address[0])

            app.debugg_write("{} sent: {}".format(self.client_address[0], str(self.data)))
            try:
                send_key(ord(self.data))
            except ValueError:
                app.debugg_write("wrong type of data: ", type(self.data))

            socket = self.request
            socket.sendto(ctypes.c_bool(True), self.client_address)
            
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


    
    
