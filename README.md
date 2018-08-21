# Photo


# Http Url
  localhost: http://pic.clvspk.com/

# Server Storage
  basePath: home:photo:img

#nginx
    
    server {
            listen  80;
            server_name  pic.clvspk.com;
            access_log  /nginx-log/pic.log  main;
            location /api {
                proxy_pass http://127.0.0.1:8085;
    			proxy_redirect off;    
    			proxy_set_header Host $http_host;    
    			proxy_set_header X-Real-IP $remote_addr;    
    			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;    
    			client_max_body_size 10m;    
    			client_body_buffer_size 128k;    
    			proxy_connect_timeout 90;    
    			proxy_send_timeout 90;    
    			proxy_read_timeout 90;    
    			proxy_buffer_size 4k;    
    			proxy_buffers 4 32k;    
    			proxy_busy_buffers_size 64k;    
    			proxy_temp_file_write_size 64k;
            }
    		location / {
            	root  /;
    	    	rewrite ^/(.*)$ /home/photo/img/$1 break;
            }
    	
    		     
            error_page   500 502 503 504  /50x.html;
            location = /50x.html {
                root   /usr/share/nginx/html;
            }
    
    }
