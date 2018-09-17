If user enters same letter more than once and it is not contained in our guess word, they still loose points. This is made to make the game more difficult.
Design decisions:
1. Hashmap for masked word for fast access and edit while user is playing.
2. HashMap of <Topic, arrayOfPgrases> for fast lookup of topics and easy random selection by index from words/phrases
3. I have not made my code particularly modular because the implementation is around 100 lines.