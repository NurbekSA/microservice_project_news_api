package model

import model.newsComponents.blockNewsComponents._
import model.newsComponents.CommentModel

case class BlockNewsAssemblyModel(primeBlock: NewsBlockPrimeModel,
                                  blocks: List[NewsBlockModel],
                                 comments: List[CommentModel]
                              )
