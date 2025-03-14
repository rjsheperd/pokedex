
### Datomic Setup

1. Download Datomic: 
```bash
mkdir ~/datomic/
cd ~/datomic
curl https://datomic-pro-downloads.s3.amazonaws.com/1.0.7277/datomic-pro-1.0.7277.zip -o datomic.zip
```

2. Unzip file.
```bash
unzip datomic.zip
```

3. Make datomic available on your PATH
```bash
export $PATH=$HOME/datomic/datomic-pro-1.0.7277/bin:$PATH
```

3. Run the transactor
```
cd <project-dir>
transactor config/transactor.properties
```
