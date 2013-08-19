#!/bin/bash
set -e
set -x

DATETIME=$(date +%Y%m%d-%H%m)
BACKUP_DIRECTORY=/opt/backups
BACKUP_FILE=$BACKUP_DIRECTORY/${DATETIME}_${DB}.sql
mysqldump -u stackUser -pstackmy5ql stackexchange_prod > $BACKUP_FILE
