"""
This module defines computation of flesch readability index
http://users.csc.calpoly.edu/~jdalbey/305/Projects/FleschReadabilityProject.html
"""
import re
import os

def compute_flesch_readability_index(sentences, words, syllables):
    return round(206.835 - (84.6 * (float(syllables)/words)) - (1.015 * (float(words)/sentences)))

def count_chars(text):
    return len(text)

def count_sentences(text):
    sentences = re.split("[.!?:;]", text)  # This handles the case of last sentence without ending with punctutation as well
    # return len(sentences) # failes for sentences ending with punctuation, counts empty string
    return sum(map(lambda sentence: 1 if sentence != "" else 0, sentences))

def remove_punctuation(text):
    # https://www.kite.com/python/answers/how-to-convert-a-string-to-a-list-of-words-without-punctuation-in-python#:~:text=split()%20to%20convert%20a,string%20without%20any%20punctuation%20marks.
    # https://docs.python.org/3/howto/regex.html#matching-characters
    return re.sub("[^\w\s]", "", text)

def count_words(text):
    text = remove_punctuation(text)
    # To handle the case where we have consecutive spaces (a b) -> 2, but (a  b) ->3, we don't count empty strings
    # Note: Python split() method splits on whitespace characters  so that already includes consecutive spaces
    # return sum(map(lambda word: 1 if word != "" else 0, remove_punctuation(text).split(" ")))
    return sum(map(lambda word: 1, remove_punctuation(text).split()))# would also work

def count_syllables_in_word(word):

    word = word.lower()

    if len(word) <= 3:
        return 1

    vowels = set("aeiouy")

    count = 0
    for char in list(word):
        if char in vowels:
            count += 1

    for i in range(len(word)-1):
        if word[i] in vowels and word[i+1] in vowels:
            count -= 1

    if word.endswith("es") or word.endswith("ed") or word.endswith("e"):
        count -= 1

    if word.endswith("le"):
        count += 1

    return count


def count_syllables(text):
    # terminal punctuation has to be removed, otherwise str.endswith() won't work if words end with punctuation
    text = re.sub("[.!?:;]$", "", text)
    # apostrophe's have to be removed as well, as cache's won't work right. (Removing this messes up test from handout
    text = re.sub("'", "", text)
    words = text.split() # split on any number of white space characters
    syllables_in_words = map(count_syllables_in_word, words)
    return sum(syllables_in_words)


def print_details(text):
    characters = count_chars(text)
    sentences = count_sentences(text)
    words = count_words(text)
    syllables = count_syllables(text)
    readability_index = compute_flesch_readability_index(sentences, words, syllables)

    print(f"Characters: {characters}")
    print(f"Sentences: {sentences}")
    print(f"Words: {words}")
    print(f"Syllables: {syllables}")
    print(f"Readability Index: {readability_index}")
    print()

if __name__=="__main__":
    data_folder = "data"
    for file in ["romeo_and_juliet.txt", "tom_sawyer.txt", "beowulf.txt", "platos_republic.txt"]:
        with open(os.path.join(data_folder, file), "r") as f:
            test = f.read()
        print(file[:-4].capitalize())
        print_details(test)

