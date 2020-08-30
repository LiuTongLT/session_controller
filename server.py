from http.server import BaseHTTPRequestHandler, HTTPServer

class myHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        import time
        time.sleep(1)
        self.send_response(200)
        #we must call end_headers() if we do not call send_header() after send_response()
        #otherwise, the Java client will have the error "received header no bytes"
        self.end_headers()
        print('Sent the response!')
    


server = HTTPServer(('', 8081), myHandler)

print('Started LTE-B server on port ' , 8081)
server.serve_forever()