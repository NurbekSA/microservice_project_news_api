package model

import model.newsComponents._
case class NewsAssemblyModel(news: NewsModel,
                     comments: List[CommentModel]
                    )

