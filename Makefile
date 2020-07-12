ps:
	@echo Listando containers na responsa do ${USER} ...
	docker-compose ps
	
start:
	docker-compose -f docker-compose.yml up -d

down:
	docker-compose down

build:
	docker-compose -f docker-compose.yml build

log-today:
	cat ./logs/archived/app-${ts}.log

docker-log:
	docker-compose logs boleto-app

write-docker-log:
	@echo gerando arquivo ./app.log ...
	docker-compose logs boleto-app >> app.log

bash:
	docker-compose -f docker-compose.yml run boleto-app bash
