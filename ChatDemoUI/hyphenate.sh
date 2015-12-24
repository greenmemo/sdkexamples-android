#!/bin/bash
#change package name from com.easemob... to com.hyphenate...
#build ant need env var ANDROID_HOME, please change it to according your enviroment.



# migrate sdk
(
    echo "list files..."
    filelist=`find res -type f -name "*.xml"`
    filelist+=`find src -type f -name "*.java"`
    filelist+= AndroidManifest.xml

    for file in $filelist; do
        echo $file
        sed -i "" 's/easemob/hyphenate/g' $file
    done

    mv src/com/easemob src/com/hyphenate
)


