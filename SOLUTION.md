# Solution comments

## Assignment 1
No comments. Just a fork.

## Assignment 2
I implemented them as classes (dropping the interfaces for the sake of simplicity as I don't really 
use the interfaces anywhere else). The model is created and initialized using a static factory method.  
The relations are the following:

1. A Canton has many Districts. A District belongs to a single Canton.
2. A District has many PoliticalCommunity. A PoliticalCommunity belongs to a single District.
3. A PoliticalCommunity has many PostalCommunity. A PostalCommunity has many PoliticalCommunity.

SQL like relations:
`Canton <- OneToMany -> District <- OneToMany -> PoliticalCommunity <- ManyToMany -> PostalCommunity`

Canton primary key is the canton code as it is unique in Switzerland.  
District primary key is the district number as it is unique.  
PoliticalCommunity primary key is the incremental column value GDENR (unique across the table).  
PostalCommunity primary key is the combination of zip code and zip code addition, corresponding to
the Swiss postal code.  

Note: the many to many is implemented only on postal community as it is not accessed / used in code 
for the political community.
Eventually, it would need a second iteration in the factory method to create the relations on that
side as the provided data doesn't have that.  

## Assignment 3
The implementation is done with streams as they are really cool and easy to read. They can be almost
interpreted as "queries". Argument checks are implemented in a naive way where needed.

## Assignment 4
I've added a maven plugin that format my code according to the google style guideline whenever I
build the project.

## Assignment 5
The political voting system is pretty complicated in Switzerland. Not every postal community might 
have its "indipendent" political community but would fall under a bigger one. For example, 
Ligornetto has a postal community but its political community are Mendrisio city and Stabio, both 
part of the Mendrisiotto region.  
It looks like the name and the short name for the political communities are always the same.  :w
