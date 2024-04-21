package model

import model.newsComponents._
case class NotifyingNewsAssemblyModel(news: NotifyingNewsModel,
                              comments: List[CommentModel]
                             )

