# Backup Tracker

## Displays

**Main**

```

---------------------------------------------------------------------
|Backup Tracker|

 || sources ||

 C:\Documents          | 3/3 backups synced
 C:\Music              | 2/3 backups synced
 C:\Movies             | 3/3 backups synced

 || backups ||

 F:\backups                        | up to date
 /Volumes/BACKUPS                  | not mounted
 /Volumes/OFFSITE_BACKUPS          | out of date!
 /Volumes/OFFSITE_BACKUPS/Music    | up to date
 /Volumes/OFFSITE_BACKUPS/Movies   | up to date
 /Volumes/OFFSITE_BACKUPS/TV       | up to date

 [s] sources
 [b] backups
 [i] settings

 select (a|b|i): _

---------------------------------------------------------------------
```

**Sources**

```

---------------------------------------------------------------------
|Backup Tracker|Sources|

 [1] C:\Documents          | 3/3 backups synced
 [2] C:\Music              | 2/3 backups synced
 [3] C:\Movies             | 3/3 backups synced
 [+] Add Source

 select (1-3|+): _

---------------------------------------------------------------------
```

**Backups**

```

---------------------------------------------------------------------
|Backup Tracker|Backups|

 [1] F:\backups                        | up to date
 [2] /Volumes/BACKUPS                  | not mounted
 [3] /Volumes/OFFSITE_BACKUPS          | out of date!
 [4] /Volumes/OFFSITE_BACKUPS/Music    | up to date
 [5] /Volumes/OFFSITE_BACKUPS/Movies   | up to date
 [6] /Volumes/OFFSITE_BACKUPS/TV       | up to date
 [+] Add Backup

 select (1-6|+): _

---------------------------------------------------------------------
```

**Backup Detail**

```

---------------------------------------------------------------------
|Backup Tracker|Backups|

 path: /Volumes/OFFSITE_BACKUPS/Music
 status: up to date
 last sync: 4/1/2018 3:00PM

 [b] backup
 [p] edit path
 [s] edit sources
 [-] remove

 select (!|p|s|-): _

---------------------------------------------------------------------
```

**Backup Edit - Path**

```

---------------------------------------------------------------------
|Backup Tracker|Backups|

 backup path [/Volumes/OFFSITE_BACKUPS/Music]: _

---------------------------------------------------------------------
```

**Backup Edit - Sources**

```

---------------------------------------------------------------------
|Backup Tracker|Backups|Sources|

 backup path: /Volumes/OFFSITE_BACKUPS/Music

 [1] C:\Music
 [2] D:\Music
 [+] Add Source

 select (1-2|+): _

---------------------------------------------------------------------
```

**Backup Edit - Source Detail**

```

---------------------------------------------------------------------
|Backup Tracker|Backups|Sources|

 backup path: /Volumes/OFFSITE_BACKUPS/Music
 source path: C:\Music
 ignore pattern: [none]

 [s] edit source
 [-] remove source from backup

 select (s|-): _

---------------------------------------------------------------------
```

**Source Detail**

```

---------------------------------------------------------------------
|Backup Tracker|Sources|

 source path: C:\Music
 ignore pattern: []

 [p] edit path
 [i] edit ignore pattern
 [d] check for duplicate files

 select (p|i|d): _

---------------------------------------------------------------------
```

**Source Edit - Path**

```

---------------------------------------------------------------------
|Backup Tracker|Sources|

 path [C:\Music]: _

---------------------------------------------------------------------
```

**Source Edit - Ignore Pattern**

```

---------------------------------------------------------------------
|Backup Tracker|Sources|

 path: C:\Music

 ignore pattern []: _

---------------------------------------------------------------------
```

**Source Edit - Duplicate Files**

```

---------------------------------------------------------------------
|Backup Tracker|Sources|Duplicate Files|

 source path: C:\Music

 [1] 01-Track-One.mp3
 [2] 02-Track-Two.mp3

---------------------------------------------------------------------
```

**Duplicate File Detail**

```

---------------------------------------------------------------------
|Backup Tracker|Sources|Duplicate Files|

 source path: C:\Music
 filename: 01-Track-One.mp3

 [1] C:\Music\WeirdAl\01-Track-One.mp3
     size: 4.2MB
     created: 1/14/2002
     updated: 1/14/2002

 [2] C:\Music\Weird Al\01-Track-One.mp3
     size: 4.2MB
     created: 1/14/2005
     updated: 1/14/2005

  select (1-2): _

---------------------------------------------------------------------
```

**Duplicate File Detail**

```

---------------------------------------------------------------------
|Backup Tracker|Sources|Duplicate Files|

 path: C:\Music\WeirdAl\01-Track-One.mp3
 size: 4.2MB
 created: 1/14/2002
 updated: 1/14/2002

 [r] rename file
 [i] ignore file
 [d] delete file

---------------------------------------------------------------------
```

## Data Backend

* sources: path
* backups: path
* backup_sources: backup, source, last_synced
* store in json file
* use **akka** for communicating state changes!

## Computations

 * each source is a git repo
 * each backup is a bare git repo
