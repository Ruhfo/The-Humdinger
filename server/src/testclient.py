import socket
import ctypes


#Very basic client, only for testing server's functionality

def client():
    HOST, PORT = str(socket.gethostbyname(socket.gethostname())), 12345

    #Join TCP/IP server
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        sock.connect((HOST, PORT))
        sock.sendall(ctypes.c_bool(True))
        sock.sendall(ctypes.c_char(160))
        received = sock.recv(1)
        print(received)
    finally:
        sock.close()

if __name__ == "__main__":
    client()
