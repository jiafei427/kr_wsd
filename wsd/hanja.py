# -*- encoding: utf-8 -*-
# -*- To Use Koma, it need to eliminates all the Hanja in the corpora -*-
import sys
from os import linesep

HANJA_START = u'\u4E00'
HANJA_END = u'\u9FFF'

if __name__=="__main__":
    for line in sys.stdin:
        uline = line.decode("utf-8")
        for ch in uline:
            if HANJA_START <= ch <= HANJA_END:
                sys.stderr.write(ch.encode("utf-8"))
                sys.stderr.write(linesep)
            else:
                sys.stdout.write(ch.encode("utf-8"))
