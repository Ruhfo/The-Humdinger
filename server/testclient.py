import socket
import ctypes

def client():
    HOST, PORT = "localhost", 9999

    #Join TCP/IP server
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        sock.connect((HOST, PORT))
        sock.sendall(ctypes.c_bool(False))
        received = sock.recv(1)
        print(received)
    finally:
        sock.close()

if __name__ == "__main__":
    client()


    
