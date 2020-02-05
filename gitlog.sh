#!/bin/bash
# see https://blog.csdn.net/weixin_30252709/article/details/95332780
# Copied from https://github.com/LinkinStars/springBootTemplate/edit/master/gitlog.sh
## Author LinkinStar

# solve the space by IFS
IFS=`echo -en "\n\b"`
echo -en $IFS

if [ -f "CHANGELOG.md" ];then
    rm -f CHANGELOG.md
    touch CHANGELOG.md
else
    touch CHANGELOG.md
fi

function printFeat(){
    for i in ${feat[@]}
    do
        echo "- "$i >> CHANGELOG.md
    done
    echo >> CHANGELOG.md
}

function printFix(){
    for i in ${fix[@]}
    do
        echo "- "$i >> CHANGELOG.md
    done
    echo >> CHANGELOG.md
}

function printOther(){
    for i in ${other[@]}
    do
        echo "- "$i >> CHANGELOG.md
    done
    echo >> CHANGELOG.md
}

function checkLog(){
    if [[ $1 == "feat"* ]]
    then
        feat[featIndex]=$1
        let featIndex++
    elif [[ $1 == "fix"* ]]
    then
        fix[fixIndex]=$1
        let fixIndex++
    else
        other[otherIndex]=$1
        let otherIndex++
    fi
}

function printLog(){
    if [[ $featIndex -ne 0 ]]; then
        echo "### Features" >> CHANGELOG.md
        printFeat
    fi

    if [[ $fixIndex -ne 0 ]]; then
        echo "### Bug Fixes" >> CHANGELOG.md
        printFix
    fi

    if [[ $otherIndex -ne 0 ]]; then
        echo "### Other Changes" >> CHANGELOG.md
        printOther
    fi

    feat=()
    featIndex=0

    fix=()
    fixIndex=0

    other=()
    otherIndex=0
}

curDate=""
function checkDate()
{
    if [[ $curDate = $1 ]]; then
        return
    fi
    curDate=$1

    printLog

    echo >> CHANGELOG.md
    echo "## "$curDate >> CHANGELOG.md
}

commitMessageList=`git log --date=format:'%Y-%m-%d' --pretty=format:'%cd%n%s'`

index=0

for i in ${commitMessageList[@]}
do
    if [[ $index%2 -eq 0 ]]
    then
        checkDate $i
    else
        #echo "- "$i >> CHANGELOG.md
        checkLog $i
    fi

    let index++
done

printLog


