import socketserver
import socket
import threading
import ctypes

class TCPHandler(socketserver.BaseRequestHandler):
    """This class handles TCP requests and receives keypresses"""
    def handle(self):
        #Data is send as c_byte value
        self.alive = self.request.recv(2).value()
        if self.alive.value() == True:
            self.data = self.request.recv(1).value()
            print (self.data)
        self.request.sendall(c_bool(True)) #Sendalive ping
class ThreadedTCPServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    #Overwrite default tcp server's methods with thread friendly variants
    pass


if __name__ == "main":
    HOST, PORT = "localhost", 9999

    server = ThreadedTCPServer((HOST, PORT), TCPHandler)
    ip, port = server.server_address

    #Start threaded server
    server_thread = threading.Thread(target=server.serve_forever)
    #Make server thread exit when main thread terminates
    server_thread.daemon = True
    server_thread.start()

    
    
