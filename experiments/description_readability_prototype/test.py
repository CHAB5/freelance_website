import pytest
from flesch_readability_index import *

tests = [
    dict(
        data="This is a sentence. \n " \
             "This is an exclamation! \n" \
             "This is a question? \n" \
             "This is just a statement:  \n" \
             "This is the first clause; \n" \
             "followed by a second clause \n",
        sentences=6,
        words=27,
        syllables=38,
        flesch_index=83
    ),
    dict(
        # http://users.csc.calpoly.edu/~jdalbey/305/Projects/FleschReadabilityProject.html
        data="#$can't9* \"ain't,\" 234ABC 23abn45 @#$aba34dfs#$% @a@e@i@o@u@",
        sentences=1,
        words=6,
        syllables=11,
        flesch_index=46
    ),
    dict(
        # https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests#cite_note-10
        data="The cat sat on the mat.",
        sentences=1,
        words=6,
        syllables=6,
        flesch_index=116
    ),
    dict(
        # https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests#cite_note-10
        data="The Australian platypus is seemingly a hybrid of a mammal and reptilian creature.",
        sentences=1,
        words=13,
        syllables=24,
        flesch_index=37
    ),
    dict(
        # https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests#cite_note-10
        data="This sentence, taken as a reading passage unto itself, is being used to prove a point.",
        sentences=1,
        words=16,
        syllables=23,
        flesch_index=69
    ),
    dict(
        data="creature.",
        sentences=1,
        words=1,
        syllables=2,
        flesch_index=37
    )
]


@pytest.mark.parametrize("test", tests)
def test_custom_sentence(test):
    assert count_sentences(test["data"]) == test["sentences"]
    assert count_words(test["data"]) == test["words"]
    assert count_syllables(test["data"]) == test["syllables"]
    assert compute_flesch_readability_index(
        count_sentences(test["data"]), count_words(test["data"]), count_syllables(test["data"])
    ) == test["flesch_index"]
