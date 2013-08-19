#!/bin/bash
set -e
set -x

DATE=$(date +%Y%m%d)
DATETIME=$(date +%Y%m%d-%H%m)
BACKUP_DIRECTORY=/opt/backups
DB=stackexchange_prod
BACKUP_FILE=$BACKUP_DIRECTORY/${DATETIME}_${DB}.sql
mysqldump -u stackUser -pstackmy5ql stackexchange_prod > $BACKUP_FILE

S3_BUCKET_URL=s3://twitter_db_backup/$DATE/
s3cmd put ${BACKUP_FILE} $S3_BUCKET_URL 2>&1
